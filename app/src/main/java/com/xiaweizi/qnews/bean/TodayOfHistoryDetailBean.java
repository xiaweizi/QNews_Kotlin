package com.xiaweizi.qnews.bean;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    TodayOfHistoryDetailBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/9
 * 创建时间： 18:01
 */

public class TodayOfHistoryDetailBean {

    /**
     * reason : success
     * result : [{"e_id":"13376","title":"法国国王路易六世出生","content":"    在935年前的今天，1081年12月1日 (农历十月廿八)，法国国王路易六世出生。\r\n    1081年12月01日（距今935年）\u2014\u2014法国国王路易六世(Louis The Fat， 法语作Louis le Gros，1081\u2014\u20141137)出生。\r\n    法国国王(1108∼1137年在位)。1098年由其父腓力一世指定为继承人，早在1108年腓力去世前，他已是实际统治者。他很快认识到首要的事情是使拥有王家领地的那些难以驾驭的贵族就范。他在位期间，大部分时间花费在与贵族进行斗争上。他实行绥靖政策，与教会和教士保持良好关系。\r\n    路易六世致力于巩固法国的王权。1109年\u20141112年，他与英格兰国王亨利一世作战。在法国国内，他顺利地进行反对有独立倾向的诸侯的斗争。路易六世给城市居民以自治权，使他们能够在他与贵族的斗争中站在他的一边。依赖市民和教会的支持，路易逐一拆毁诸侯的城堡，并强制在他们的领地上驻扎忠于王室的卫队。至路易六世去世时。卡佩王朝在法国的统治已经比较稳定。\r\n    从1130年开始，圣丹尼斯修道院院长叙热成为路易六世的主要顾问。叙热在扩大王权方面颇有成效，还为路易写了一部传记。路易六世的遗体也安葬在圣丹尼斯修道院。\r\n    \r\n\r\n","picNo":"1","picUrl":[{"pic_title":"","id":1,"url":"http://images.juheapi.com/history/13376_1.jpg"}]}]
     * error_code : 0
     */

    private String reason;
    private int error_code;
    private List<ResultBean> result;

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "TodayOfHistoryDetailBean{" +
                "reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", result=" + result +
                '}';
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", picNo='" + picNo + '\'' +
                    ", picUrl=" + picUrl +
                    '}';
        }

        /**
         * e_id : 13376
         * title : 法国国王路易六世出生
         * content :     在935年前的今天，1081年12月1日 (农历十月廿八)，法国国王路易六世出生。
         1081年12月01日（距今935年）——法国国王路易六世(Louis The Fat， 法语作Louis le Gros，1081——1137)出生。
         法国国王(1108∼1137年在位)。1098年由其父腓力一世指定为继承人，早在1108年腓力去世前，他已是实际统治者。他很快认识到首要的事情是使拥有王家领地的那些难以驾驭的贵族就范。他在位期间，大部分时间花费在与贵族进行斗争上。他实行绥靖政策，与教会和教士保持良好关系。
         路易六世致力于巩固法国的王权。1109年—1112年，他与英格兰国王亨利一世作战。在法国国内，他顺利地进行反对有独立倾向的诸侯的斗争。路易六世给城市居民以自治权，使他们能够在他与贵族的斗争中站在他的一边。依赖市民和教会的支持，路易逐一拆毁诸侯的城堡，并强制在他们的领地上驻扎忠于王室的卫队。至路易六世去世时。卡佩王朝在法国的统治已经比较稳定。
         从1130年开始，圣丹尼斯修道院院长叙热成为路易六世的主要顾问。叙热在扩大王权方面颇有成效，还为路易写了一部传记。路易六世的遗体也安葬在圣丹尼斯修道院。



         * picNo : 1
         * picUrl : [{"pic_title":"","id":1,"url":"http://images.juheapi.com/history/13376_1.jpg"}]
         */



        private String title;
        private String content;
        private String picNo;
        private List<PicUrlBean> picUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPicNo() {
            return picNo;
        }

        public void setPicNo(String picNo) {
            this.picNo = picNo;
        }

        public List<PicUrlBean> getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(List<PicUrlBean> picUrl) {
            this.picUrl = picUrl;
        }

        public static class PicUrlBean {
            @Override
            public String toString() {
                return "PicUrlBean{" +
                        "pic_title='" + pic_title + '\'' +
                        ", id=" + id +
                        ", url='" + url + '\'' +
                        '}';
            }

            /**
             * pic_title :
             * id : 1
             * url : http://images.juheapi.com/history/13376_1.jpg
             */



            private String pic_title;
            private int id;
            private String url;

            public String getPic_title() {
                return pic_title;
            }

            public void setPic_title(String pic_title) {
                this.pic_title = pic_title;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
