# 视频生成接口 v3（本站 /api/v3/contents/generations/tasks）

本站对外开放的视频生成接口，**请求/响应结构对齐火山方舟（Ark）视频生成 API**，
接入方可直接复用火山 SDK 的数据结构，只需替换 base_url 与 API Key。

内部逻辑与 v1（`/api/v1/video/tasks`）完全一致，共用 `lib/video-create.ts`：
计价 → 原子扣积分 → 建任务 → 按渠道 `priority` 降级提交 → 轮询 → 失败自动退款 + 飞书告警。
v3 只负责「把火山结构翻译成内部入参」和「把内部结果翻译回火山结构」。

上游原始接口说明见同目录 `generation.md` / `query.md` / `querylist.md` / `delete.md`（只读参考资料）。
v1 的 JWT 版接口见 `docs/video-api.md`。

## 接口一览

| 接口 | 说明 |
| --- | --- |
| `POST /api/v3/contents/generations/tasks` | 创建视频生成任务（异步） |
| `GET /api/v3/contents/generations/tasks/{id}` | 查询单个任务 |
| `GET /api/v3/contents/generations/tasks` | 分页查询任务列表 |
| `DELETE /api/v3/contents/generations/tasks/{id}` | 删除任务记录 |

## 鉴权

全部接口走 **API Key 鉴权**（`lib/apikey-auth.ts`），不接受 v1 的 JWT：

```http
Authorization: Bearer <API_KEY>
Content-Type: application/json
```

密钥在「账户 → API 密钥」创建（明文仅创建时返回一次，库里只存 sha256）。
密钥被禁用（`status != active`）同样返回 401。

错误响应统一为火山 Ark 风格：

```json
{ "error": { "code": "InvalidParameter", "message": "..." } }
```

| HTTP | `error.code` | 触发场景 |
| --- | --- | --- |
| 400 | `InvalidParameter` | 请求体非法 JSON、缺 `model` / `content`、参数组合不被任何渠道支持、参考素材超限、`content[]` 场景混用 |
| 400 | `ModelNotOpen` | 模型存在，但没有一条渠道对 v3 开放（`model_routes.version`） |
| 401 | `AuthenticationError` | 未带 API Key / 密钥无效 / 密钥已禁用 |
| 402 | `InsufficientQuota` | 积分不足，`message` 含本次所需积分 |
| 404 | `NotFound` | 任务不存在或不属于当前密钥归属用户 |

---

## 1. 创建视频生成任务

```http
POST /api/v3/contents/generations/tasks
```

异步接口，**调用后立即返回任务 id**，需通过查询接口轮询结果。创建成功即扣积分，任务失败自动全额退还。

### 请求体

```json
{
  "model": "seedance-2-pro",
  "content": [
    { "type": "text", "text": "一只猫在草地上奔跑，镜头缓慢推近" },
    {
      "type": "image_url",
      "role": "first_frame",
      "image_url": { "url": "https://cdn.example.com/first.jpg" }
    }
  ],
  "duration": 5,
  "ratio": "16:9",
  "resolution": "720p",
  "generate_audio": true
}
```

| 字段 | 类型 | 必填 | 默认值 | 说明 |
| --- | --- | :--: | --- | --- |
| `model` | string | ✅ | — | 模型 ID，取值见 `GET /api/v1/models` |
| `content` | object[] | ✅ | — | 生成素材数组，非空；结构见下 |
| `duration` | number | | `5` | 视频时长（秒） |
| `ratio` | string | | `"16:9"` | 画面比例 |
| `resolution` | string | | `"720p"` | 分辨率 |
| `generate_audio` | boolean | | `true` | 是否生成同步音频 |

> `duration` / `ratio` / `resolution` 的可选值取决于模型与渠道配置，任一项不被支持时返回 400
> `InvalidParameter`（message：`模型不存在、已下线或不支持该分辨率/比例/时长`）。

**类型不符的字段会静默走默认值**（例如 `duration: "5"` 传字符串按 5 处理，
`resolution: 1080` 按 `"720p"` 处理），不会报错——请确保类型正确。

### content[] 结构

支持 4 种元素类型：

| `type` | 取值字段 | `role` | 说明 |
| --- | --- | --- | --- |
| `text` | `text` | — | 文本提示词，多条按出现顺序用 `\n` 拼接 |
| `image_url` | `image_url.url` | `first_frame` / `last_frame` / `reference_image` / 不填 | 图片素材 |
| `video_url` | `video_url.url` | `reference_video` / 不填 | 参考视频 |
| `audio_url` | `audio_url.url` | `reference_audio` / 不填 | 参考音频 |

素材 URL 需**公网可访问**，可先调 `POST /api/v1/upload` 上传到 COS。
上游 `draft_task` 类型（样片任务 ID）本站不支持，传入会被忽略。

### 场景与 role 映射（`lib/content-task.ts` 的 `parseContent`）

本站内部用 `mode` + 图片顺序表达场景，v3 按 `role` 反推：

| 传入的 role 组合 | 推断 mode | 上游实际场景 |
| --- | --- | --- |
| `first_frame` + `last_frame`（各 1 张） | `first_last_frame` | 图生视频-首尾帧 |
| 单个 `first_frame` | `multi_ref` | 图生视频-首帧（渠道按图片数派生 `image_to_video`） |
| 仅 `reference_image` / 未填 role 的图片 | `multi_ref` | 多模态参考生视频 |
| `reference_video` / `reference_audio` | `multi_ref` | 多模态参考生视频 |
| 只有 `text` | `multi_ref` | 文生视频（渠道派生 `text_to_video`） |

**帧类（`first_frame` / `last_frame`）与参考类（参考图 / 视频 / 音频）互斥，不可混用**，
与上游约束一致。违反时返回 400：

| message | 原因 |
| --- | --- |
| `图生视频-首帧、图生视频-首尾帧、多模态参考生视频为 3 种互斥场景，不可混用。` | 帧类与参考类同时出现 |
| `仅提供 last_frame 无效，图生视频-首尾帧需同时提供首帧和尾帧。` | 只给了尾帧 |
| `图生视频-首帧/首尾帧需提供有效的图片 URL。` | 声明了帧类 role 但 url 为空 |
| `参考素材数量超过该模型限制（图片/视频/音频超上限）` | 超出渠道 `media_limits` |

### 提示词约束

- **`text` 类型不可全部省略**：内部要求 prompt 非空，纯图/纯视频输入会返回 400
  `InvalidParameter`（message：`model 和 prompt 不能为空`）。这是与上游「文本可选」的差异，
  上游允许纯素材输入，本站暂不支持。
- 长度上限 **2500 字**，超出返回 400（message：`提示词不能超过 2500 字`）。
- 提示词里的中文占位符（如 `@图片1`、`@视频1`）会按所选渠道的 `prompt_ref_map` 自动转成
  上游要求的引用格式（如 `@image_file_1`），接入方无需手动处理。

### 未支持的上游参数

以下上游字段**本站不读取**，传入会被忽略（不报错），响应中固定返回类型空值：

`seed`、`callback_url`、`return_last_frame`、`service_tier`、`execution_expires_after`、
`draft`、`tools`（含 `web_search`）、`safety_identifier`、`priority`、`camera_fixed`。

因此**没有回调通知能力**，只能轮询；也**拿不到尾帧图像**（`content.last_frame_url` 恒为 `""`）。

### 响应

```json
{ "id": "V1StGXR8_Z5jdHi6B-myT" }
```

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | string | 任务 ID（nanoid，21 位），用于查询/删除 |

创建接口**只返回 id**，与上游一致。注意积分扣减发生在返回 200 之前，
但上游提交是异步的：返回 200 只代表「任务已建立、积分已扣」，不代表已成功提交上游。
提交失败（所有渠道均失败）会异步置 `failed` 并退款，需通过查询接口感知。

---

## 2. 查询单个任务

```http
GET /api/v3/contents/generations/tasks/{id}
```

### 响应

```json
{
  "id": "V1StGXR8_Z5jdHi6B-myT",
  "model": "seedance-2-pro",
  "status": "succeeded",
  "error": null,
  "created_at": 1769817600,
  "updated_at": 1769817712,
  "content": {
    "video_url": "https://cos.example.com/videos/xxx.mp4",
    "last_frame_url": ""
  },
  "seed": 0,
  "resolution": "720p",
  "ratio": "16:9",
  "duration": 5,
  "frames": 0,
  "framespersecond": 0,
  "generate_audio": true,
  "tools": [],
  "safety_identifier": "",
  "priority": 0,
  "draft": false,
  "draft_task_id": "",
  "service_tier": "default",
  "execution_expires_after": 0,
  "usage": { "completion_tokens": 0, "total_tokens": 0 }
}
```

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | string | 任务 ID |
| `model` | string | 本站模型 ID（非上游模型名） |
| `status` | string | 任务状态，见下表 |
| `error` | object \| null | 失败时为 `{ code, message }`，其余为 `null` |
| `created_at` / `updated_at` | integer | Unix 时间戳（秒） |
| `content.video_url` | string | 成品视频地址（已转存 COS），未完成时为 `""` |
| `content.last_frame_url` | string | 恒为 `""`（本站不保存尾帧） |
| `resolution` / `ratio` / `duration` / `generate_audio` | — | 回显创建时的参数 |
| `usage` | object | 恒为 0（本站不统计 token，按积分计费） |

### status 枚举

存储层状态到火山枚举的映射（`lib/content-task.ts`）：

| 本站状态 | 返回值 | 说明 |
| --- | --- | --- |
| `pending` | `queued` | 已建任务，尚未提交上游（瞬时态） |
| `processing` | `running` | 上游生成中 |
| `done` | `succeeded` | 完成，`content.video_url` 可用 |
| `failed` | `failed` | 失败，积分已退还，`error.message` 为原因 |
| — | `cancelled` / `expired` | 枚举保留，本站当前不产生这两种状态 |

### 与上游的差异

- **无 7 天查询窗口**：上游只保留最近 7 天任务，本站任务记录长期保留（除主动 DELETE 软删除）。
- **`content.video_url` 无有效期**：上游 URL 24 小时过期，本站已把结果转存腾讯云 COS，
  返回的是永久地址，无需急着下载转存。
- **`error.code` 恒为 `""`**：`error.message` 是归一化后的中文文案
  （`lib/video-providers/error-map.ts`，如「内容敏感未通过审核，请修改提示词或参考素材后重试」），
  不透传上游原始错误码。需要按错误类型分支处理的话，请匹配 message，
  或参考 `docs/openapi-error-codes.md` 了解上游原始分类。
- **`frames` / `framespersecond` / `seed` / `usage` 等为类型空值**：表里未存这些字段。

### 错误

| HTTP | `error.code` | 说明 |
| --- | --- | --- |
| 401 | `AuthenticationError` | API Key 无效 |
| 404 | `NotFound` | 任务不存在、已软删除，或不属于当前用户 |

跨用户查询一律按 404 处理（不区分「不存在」与「无权限」），避免任务 ID 探测。

---

## 3. 查询任务列表

```http
GET /api/v3/contents/generations/tasks
```

| Query 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `page_num` | 1 | 页码，取值 [1, 500] |
| `page_size` | 20 | 每页数量，取值 [1, 500] |
| `filter.status` | — | 火山状态枚举：`queued` / `running` / `succeeded` / `failed` / `cancelled` / `expired` |
| `filter.model` | — | 按模型 ID 精确匹配（上游此处是 Endpoint ID，本站为模型 ID） |
| `filter.task_ids` | — | 任务 ID 精确匹配，重复传参：`filter.task_ids=id1&filter.task_ids=id2` |

超出范围的 `page_num` / `page_size` 会被夹到区间内，不报错。
`filter.status` 传非法值不报错，按原值查库，结果为空。
上游的 `filter.service_tier` 本站不支持（无离线推理模式）。

### 响应

```json
{
  "items": [
    {
      "id": "V1StGXR8_Z5jdHi6B-myT",
      "model": "seedance-2-pro",
      "status": "succeeded",
      "error": null,
      "created_at": 1769817600,
      "updated_at": 1769817712,
      "content": { "video_url": "https://cos.example.com/videos/xxx.mp4", "last_frame_url": "" },
      "resolution": "720p",
      "ratio": "16:9",
      "duration": 5,
      "generate_audio": true,
      "usage": { "completion_tokens": 0, "total_tokens": 0 }
    }
  ],
  "total": 1
}
```

`items[]` 每一项与「查询单个任务」的结构完全一致（同一个序列化函数）。
`total` 为符合筛选条件的总数。响应**不回显 `page_num` / `page_size`**，与上游一致。

按 `id` 倒序返回（等价于创建时间倒序），只返回当前 API Key 归属用户且未软删除的任务。

---

## 4. 删除任务记录

```http
DELETE /api/v3/contents/generations/tasks/{id}
```

成功返回 **HTTP 200，无响应体**（对齐上游「本接口无返回参数」）。

| 当前状态 | 是否支持 | 行为 |
| --- | :--: | --- |
| `succeeded`（done） | ✅ | 软删除记录，后续查询返回 404 |
| `failed` | ✅ | 软删除记录 |
| `expired` | ✅ | 软删除记录 |
| `queued`（pending） | ❌ | 400 `InvalidParameter` |
| `running`（processing） | ❌ | 400 `InvalidParameter` |
| `cancelled` | ❌ | 400 `InvalidParameter` |

**本站不提供取消能力**，这是与上游最大的差异。上游 `queued` 是可取消的排队窗口，
而本站没有真实队列：任务建成后立即异步提交上游，`pending` 只是「提交上游前」的瞬时态，
一旦发到上游就无法撤回。因此 `queued` 状态调 DELETE 返回 400
（message：`The task in \`pending\` status does not support DELETE.`），
而非上游的「置为 cancelled」。

删除是**软删除**（写 `deleted_at`），数据仍在库中用于对账，但对外不可见，也不会退还积分
（已完成任务的积分不退）。

---

## 计费与退款

- 计价：`消耗积分 = cost_per_second × duration`，费率按 `模型 + 分辨率` 从后台配置读取。
- 扣费时机：创建任务时用条件更新原子扣减，余额不足直接返回 402，不建任务。
- 退款：任务失败（所有渠道提交失败 / 上游报错 / 超时 / 服务重启中断）**自动全额退还**，
  并写 `credit_logs`（`type=refund`）与 `video_task_logs`（`refunded`），同时触发飞书告警。
- 详细规则见 `docs/credits-rules.md`。

## 轮询建议

- 后台每 **5 秒**轮询上游一次，状态变化后落库；客户端**独立轮询本站查询接口**即可，
  建议间隔 3~5 秒，单次任务通常 30~120 秒完成。
- `status` 为 `succeeded` / `failed` 时停止轮询。
- 生产环境重启时 `instrumentation.ts` → `resumeProcessingTasks()` 会扫描 `processing` 任务续上轮询，
  不会因发版丢任务。

---

## 完整调用示例

```bash
BASE=https://your-domain.com
KEY=sk-xxxxxxxxxxxx

# 1. 创建任务（文生视频）
TASK_ID=$(curl -s -X POST "$BASE/api/v3/contents/generations/tasks" \
  -H "Authorization: Bearer $KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "seedance-2-pro",
    "content": [
      { "type": "text", "text": "一只猫在草地上奔跑，镜头缓慢推近" }
    ],
    "duration": 5,
    "ratio": "16:9",
    "resolution": "720p",
    "generate_audio": true
  }' | jq -r '.id')

echo "Task ID: $TASK_ID"

# 2. 轮询任务状态
while true; do
  RESULT=$(curl -s "$BASE/api/v3/contents/generations/tasks/$TASK_ID" \
    -H "Authorization: Bearer $KEY")
  STATUS=$(echo "$RESULT" | jq -r '.status')
  echo "Status: $STATUS"
  if [ "$STATUS" = "succeeded" ] || [ "$STATUS" = "failed" ]; then
    echo "$RESULT" | jq '{ status, video: .content.video_url, error }'
    break
  fi
  sleep 5
done
```

图生视频-首尾帧的 `content`：

```json
[
  { "type": "text", "text": "镜头从近景平滑推向远景" },
  { "type": "image_url", "role": "first_frame", "image_url": { "url": "https://cdn.example.com/a.jpg" } },
  { "type": "image_url", "role": "last_frame",  "image_url": { "url": "https://cdn.example.com/b.jpg" } }
]
```

多模态参考生视频的 `content`：

```json
[
  { "type": "text", "text": "@图片1 中的人物走进 @视频1 的场景" },
  { "type": "image_url", "role": "reference_image", "image_url": { "url": "https://cdn.example.com/p1.jpg" } },
  { "type": "video_url", "role": "reference_video", "video_url": { "url": "https://cdn.example.com/v1.mp4" } },
  { "type": "audio_url", "role": "reference_audio", "audio_url": { "url": "https://cdn.example.com/a1.mp3" } }
]
```

## 与 v1 接口对照

| 维度 | v1 `/api/v1/video/tasks` | v3 `/api/v3/contents/generations/tasks` |
| --- | --- | --- |
| 鉴权 | JWT（登录换 token，7 天） | API Key（长效） |
| 素材传参 | `image_urls` / `video_urls` / `audio_urls` + `mode` | `content[]` + `role`（自动推断 mode） |
| 创建响应 | `{ "taskId": "..." }` | `{ "id": "..." }` |
| 状态枚举 | `pending` / `processing` / `done` / `failed` | `queued` / `running` / `succeeded` / `failed` |
| 结果字段 | `data.images[0]` | `content.video_url` |
| 人脸识别 | `use_face` 入参，默认 `false` | 固定 `true`，仅走支持人脸的渠道 |
| 列表 / 删除 | 无 | 有 |
| 错误结构 | `{ "error": "..." }` | `{ "error": { "code", "message" } }` |
| 渠道范围 | `model_routes.version` 为 `v1` 或 `null` | `model_routes.version` 为 `v3` 或 `null` |

同一个模型可能只对其中一个版本开放渠道：v3 请求命中「模型存在但无 v3 渠道」时返回 400 `ModelNotOpen`。
