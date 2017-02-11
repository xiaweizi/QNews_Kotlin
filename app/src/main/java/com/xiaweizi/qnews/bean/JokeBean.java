package com.xiaweizi.qnews.bean;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    JokeBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/11
 * 创建时间： 13:48
 */

public class JokeBean {

    /**
     * error_code : 0
     * reason : Success
     * result : {"data":[{"content":"一日早饭后，几人宿舍闲聊。突然一人放一屁。　　为缓解尴尬我说一句：\u201c早上萝卜吃多了吧，吃萝卜容易放屁。\u201d　　另一同学说：\u201c恩，吃大蒜也容易放屁.\"　　突然一同学说：\u201c不对，是洋葱味的\u2026\u2026\u201d","hashId":"f709560f8e4a38158915971dc3ed6821","unixtime":1486785230,"updatetime":"2017-02-11 11:53:50"},{"content":"高中时有一同学特抠（怕吃亏，爱占便宜）。　　有一次哥目睹了一件超级神奇的事情，我在商店买东西，他也在买东西，他跟售货员说买一袋方便面，售货员给他一袋后，他又要了一袋，一手一袋，并惦量了一下后，还给售货员一袋说他要买他手中剩下的那袋。　　我不解啊，后来就问他，他答到：我试了下，这袋重些！我顿时无语！！！","hashId":"cfa446463d5329700437f93621f9184c","unixtime":1486785230,"updatetime":"2017-02-11 11:53:50"},{"content":"一天，老师问芳芳：\u201c芳芳，你知道我们中国有几个朝代吗？分别是哪几个呢？\u201d　　\u201c对不起，老师。\u201d芳芳红着脸：\u201c我什么也不知道。\u201d　　老师又说：\u201c那我来告诉你好了！记住啦，我们中国的五个朝代是唐宋元明清，明白了吗？\u201d　　\u201c明白了！\u201d芳芳兴奋地说：\u201c老师，我明白了！是糖醋盐味精，对吧？老师？\u201d　　老师当场晕倒\u2026\u2026","hashId":"5a46b671bb8db7fd84bc37dd99fe9031","unixtime":1486785230,"updatetime":"2017-02-11 11:53:50"},{"content":"元旦佳节，火车上坐满了南来北往的旅客，一手执气球的小姐和一老太太坐在一起。　　小姐想放屁，憋又憋不住，就用手佯装擦拭气球，随和着摩擦发出的声响连续放了几个屁。未想老太太听力绝佳，只见她一边捏着鼻子一边扇着说道：\u201c我的天！一下就放七个茶蛋屁，声音倒是入耳，可这味儿真让人无法消受？\u201d","hashId":"20b96dd7a8bf171ddf18a56f22e839a1","unixtime":1486785230,"updatetime":"2017-02-11 11:53:50"}]}
     */

    private int error_code;
    private String reason;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * content : 一日早饭后，几人宿舍闲聊。突然一人放一屁。　　为缓解尴尬我说一句：“早上萝卜吃多了吧，吃萝卜容易放屁。”　　另一同学说：“恩，吃大蒜也容易放屁."　　突然一同学说：“不对，是洋葱味的……”
             * hashId : f709560f8e4a38158915971dc3ed6821
             * unixtime : 1486785230
             * updatetime : 2017-02-11 11:53:50
             */

            private String content;
            private int unixtime;
            private String updatetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }
    }
}
