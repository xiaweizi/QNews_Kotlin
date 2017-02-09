package com.xiaweizi.qnews.bean;

import java.util.List;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    NewsDataBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/9
 * 创建时间： 10:52
 */

public class NewsDataBean {
    @Override
    public String toString() {
        return "NewsDataBean{" +
                "result=" + result +
                '}';
    }

    /**
     * reason : 成功的返回
     * result : {"stat":"1","data":[{"uniquekey":"a85a7b0809d8f9938bf073e577cd7bfb","title":"朴槿惠\u201c亲信干政\u201d事件：崔顺实主动接受传讯 口罩遮面沉默不语【组图】","date":"2017-02-09 10:45","category":"国际","author_name":"人民网","url":"http://mini.eastday.com/mobile/170209104511004.html","thumbnail_pic_s":"http://04.imgmini.eastday.com/mobile/20170209/20170209104511_b1a893219c0d9fa1e590d8813270b875_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://04.imgmini.eastday.com/mobile/20170209/20170209104511_00d84ccb8b01dc1ab1a5a4e851cb332a_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://04.imgmini.eastday.com/mobile/20170209/20170209104511_99e561e401b767cac641a446cb088a8a_3_mwpm_03200403.jpeg"},{"uniquekey":"7610e9029dc297ff33269e0a69cf9a75","title":"白宫公布被媒体\u201c忽视\u201d恐袭名单 或意在政治洗脑","date":"2017-02-09 09:06","category":"国际","author_name":"天人对话","url":"http://mini.eastday.com/mobile/170209090650201.html","thumbnail_pic_s":"http://05.imgmini.eastday.com/mobile/20170209/20170209_3b1921d573193e351ada16629f0a8a9e_cover_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://05.imgmini.eastday.com/mobile/20170209/20170209_02549dfd0bc2f7bdd0ae62ee41cd9940_cover_mwpm_03200403.jpeg"}]}
     * error_code : 0
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "data=" + data +
                    '}';
        }

        /**
         * stat : 1
         * data : [{"uniquekey":"a85a7b0809d8f9938bf073e577cd7bfb","title":"朴槿惠\u201c亲信干政\u201d事件：崔顺实主动接受传讯 口罩遮面沉默不语【组图】","date":"2017-02-09 10:45","category":"国际","author_name":"人民网","url":"http://mini.eastday.com/mobile/170209104511004.html","thumbnail_pic_s":"http://04.imgmini.eastday.com/mobile/20170209/20170209104511_b1a893219c0d9fa1e590d8813270b875_1_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://04.imgmini.eastday.com/mobile/20170209/20170209104511_00d84ccb8b01dc1ab1a5a4e851cb332a_2_mwpm_03200403.jpeg","thumbnail_pic_s03":"http://04.imgmini.eastday.com/mobile/20170209/20170209104511_99e561e401b767cac641a446cb088a8a_3_mwpm_03200403.jpeg"},{"uniquekey":"7610e9029dc297ff33269e0a69cf9a75","title":"白宫公布被媒体\u201c忽视\u201d恐袭名单 或意在政治洗脑","date":"2017-02-09 09:06","category":"国际","author_name":"天人对话","url":"http://mini.eastday.com/mobile/170209090650201.html","thumbnail_pic_s":"http://05.imgmini.eastday.com/mobile/20170209/20170209_3b1921d573193e351ada16629f0a8a9e_cover_mwpm_03200403.jpeg","thumbnail_pic_s02":"http://05.imgmini.eastday.com/mobile/20170209/20170209_02549dfd0bc2f7bdd0ae62ee41cd9940_cover_mwpm_03200403.jpeg"}]
         */



        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * uniquekey : a85a7b0809d8f9938bf073e577cd7bfb
             * title : 朴槿惠“亲信干政”事件：崔顺实主动接受传讯 口罩遮面沉默不语【组图】
             * date : 2017-02-09 10:45
             * category : 国际
             * author_name : 人民网
             * url : http://mini.eastday.com/mobile/170209104511004.html
             * thumbnail_pic_s : http://04.imgmini.eastday.com/mobile/20170209/20170209104511_b1a893219c0d9fa1e590d8813270b875_1_mwpm_03200403.jpeg
             * thumbnail_pic_s02 : http://04.imgmini.eastday.com/mobile/20170209/20170209104511_00d84ccb8b01dc1ab1a5a4e851cb332a_2_mwpm_03200403.jpeg
             * thumbnail_pic_s03 : http://04.imgmini.eastday.com/mobile/20170209/20170209104511_99e561e401b767cac641a446cb088a8a_3_mwpm_03200403.jpeg
             */

            private String title;
            private String date;
            private String category;
            private String author_name;
            private String url;
            private String thumbnail_pic_s;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            @Override
            public String toString() {
                return "DataBean{" +
                        "title='" + title + '\'' +
                        ", date='" + date + '\'' +
                        ", category='" + category + '\'' +
                        ", author_name='" + author_name + '\'' +
                        ", url='" + url + '\'' +
                        ", thumbnail_pic_s='" + thumbnail_pic_s + '\'' +
                        '}';
            }
        }
    }
}
