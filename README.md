[软件下载地址](https://github.com/xiaweizi/QNews/raw/master/app-release.apk)

扫码下载：


![扫码下载.png](http://upload-images.jianshu.io/upload_images/4043475-45607354d136266c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


更新日志：

    - 2017 4月24日
    	-  添加自定义消息推送
    - 2017 4月4日
    	- 添加分享功能
    - 2017 3月30日
    	- 添加修改头像功能
    - 2017 3月17日
    	- 修复打开应用白屏 bug
    - 2017 3月14日
    	- 历史上的今天模块添加转场动画
    	- 新闻详情页添加侧滑结束页面功能
    - 2017 2月27日
    	- 修复横竖屏切换问题
    	- 重构 `MainActivity` 代码，优化 `fragment` 切换
    - 2017 2月23日
    	- 重构网络框架： 使用`RxJava+Retrofit`进行初步封装
    - 2017 2月16日
    	- 增加主题切换功能
    	- 增加夜间模式功能
    - 2017 2月15日
    	- 添加趣图模块


>我通过一下目录进行项目的演示：

> * [1. 整体风格和个人页面](#1)
>
> * [2. 跳转界面](#2)
> 
> * [3. 新闻模块](#3) 
> 
> * [4. 段子模块](#4)
> 
> * [5. 趣图模块](#5)
>
> * [6. 历史上的今天模块](#6)
> 
> * [7. 图灵机器人模块](#7)
> 
> * [8. 主题切换以及夜间模式模块](#8)
> 
> * [9. 用到的第三方开源库](#9)

<h2  id='1'>1. 整体风格和个人页面</h2>
*整体采用的是Material Design风格，看起来好看一点*

![整体风格.gif](http://upload-images.jianshu.io/upload_images/4043475-eb1932393eedabec.gif?imageMogr2/auto-orient/strip)
>如果觉得还行，请给个star吧

<h2  id='2'>2. 跳转界面</h2>

![跳转界面.gif](http://upload-images.jianshu.io/upload_images/4043475-f8fdb6021cf5d558.gif?imageMogr2/auto-orient/strip)
>这个就是属性动画(translationX + translationY)

<h2  id='3'>3. 新闻模块</h2>

![新闻模块.gif](http://upload-images.jianshu.io/upload_images/4043475-19e7b7c64e0989fc.gif?imageMogr2/auto-orient/strip)

>数据来源: 聚合数据
>
>网络框架: okhttp
>
>图片框架: Glide

<h2  id='4'>4. 段子模块 </h2>

![段子模块.gif](http://upload-images.jianshu.io/upload_images/4043475-672753f228c57c18.gif?imageMogr2/auto-orient/strip)
>支持下拉刷新，上拉加载更多

<h2  id='5'>5. 趣图模块 </h2>
*这个是个小彩蛋，在段子界面，在点击一下段子就会跳转到趣图模块*

![趣图模块.gif](http://upload-images.jianshu.io/upload_images/4043475-811a7bef92721664.gif?imageMogr2/auto-orient/strip)
>这里是随机从服务器中获取趣图，有时候也会有动态图

<h2  id='6'>6. 历史上的今天模块 </h2>
*这部分我觉得既好玩，又很有意义*

![历史上的今天.gif](http://upload-images.jianshu.io/upload_images/4043475-2a88baf13919023b.gif?imageMogr2/auto-orient/strip)
>说实话，我还挺喜欢叶圣陶的。

>其实点进去上面是viewpager展示图片的，但是不知道怎么搞得，聚合数据图片不提供了，也是醉了。

<h2  id='7'>7. 图灵机器人模块</h2>

![图灵机器人.gif](http://upload-images.jianshu.io/upload_images/4043475-8858ce0eaeda160f.gif?imageMogr2/auto-orient/strip)
>闲来没事，单身狗可以玩一下，嘻嘻...

<h2  id='8'>8. 主题切换以及夜间模式</h2>

![换肤.gif](http://upload-images.jianshu.io/upload_images/4043475-211cf778e95c0898.gif?imageMogr2/auto-orient/strip)
>我用采用的方式比较简单，就是直接setTheme(int resId)方式，网上有一些好的实现方式，不妨上网搜一下。

<h2  id='9'>9. 用到的第三方开源库</h2>

1. 数据来源： [聚合数据](https://www.juhe.cn/docs)
2. 标题来源： [艺术字生成](http://www.akuziti.com/)
3. 小图标来源： [阿里适量图](http://www.iconfont.cn/)
4. 图片加载框架： [Glide](https://github.com/bumptech/glide)
5. 网络请求框架： [鸿洋大神的okhttputils](https://github.com/hongyangAndroid/okhttputils)
6. view注解框架： [butterknife](https://github.com/JakeWharton/butterknife)
7. 基类适配器： [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
8. 指示器： [MagicIndicator](https://github.com/hackware1993/MagicIndicator)
9. 底部栏： [BoomMenu](https://github.com/Nightonke/BoomMenu)
