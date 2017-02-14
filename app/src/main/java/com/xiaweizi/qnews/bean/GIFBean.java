package com.xiaweizi.qnews.bean;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    GIFBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/13
 * 创建时间： 17:29
 */

public class GIFBean {

    /**
     * reason : success
     * result : [{"content":"小河马也是醉了","hashId":"E160946B70816B322B271651CD287BF9","unixtime":"1433730140","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/E160946B70816B322B271651CD287BF9.jpg"},{"content":"同学，拔个河而已，用的着那么拼吗？你读书时为学校做过最拼的事是什么？","hashId":"8ED173026638E739C1E4B808CD583477","unixtime":"1433730754","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/8ED173026638E739C1E4B808CD583477.png"},{"content":"逗比该吃药了","hashId":"9AD4587218726CA90957861EFEC38619","unixtime":"1433730763","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/9AD4587218726CA90957861EFEC38619.jpg"},{"content":"单身是种罪啊","hashId":"DC6293F2709340934BB748047F3F351E","unixtime":"1433731385","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/DC6293F2709340934BB748047F3F351E.jpg"},{"content":"不说了，又特么失恋了","hashId":"A659E4E55F6E11B565294839F192AAB5","unixtime":"1433731938","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/A659E4E55F6E11B565294839F192AAB5.png"},{"content":"当你主人也不容易啊","hashId":"E79299C5ECC0942B9ED0DDEA2464235F","unixtime":"1433731945","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/E79299C5ECC0942B9ED0DDEA2464235F.gif"},{"content":"为什么觉得这好碉堡","hashId":"5EE484730AAA1157C144F3EBFF516103","unixtime":"1433731945","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/5EE484730AAA1157C144F3EBFF516103.jpg"},{"content":"兄弟，看来口臭的就是你了","hashId":"5BCFBDA0807DA3A971A620F6600546B7","unixtime":"1433731949","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/5BCFBDA0807DA3A971A620F6600546B7.jpg"},{"content":"不然我现在就把你们吃掉","hashId":"011385BB00A307D07C5EF342ECF7084A","unixtime":"1433732599","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/011385BB00A307D07C5EF342ECF7084A.jpg"},{"content":"只看到辣椒看不到鱼","hashId":"C0701A01613ECFF03D6E6CAC8AD0CCE0","unixtime":"1433733171","url":"http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/C0701A01613ECFF03D6E6CAC8AD0CCE0.jpg"}]
     * error_code : 0
     */

    private String reason;
    private int error_code;
    private List<ResultBean> result;

    public String getReason() {
        return reason;
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
        /**
         * content : 小河马也是醉了
         * hashId : E160946B70816B322B271651CD287BF9
         * unixtime : 1433730140
         * url : http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201506/08/E160946B70816B322B271651CD287BF9.jpg
         */

        private String content;
        private String url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
