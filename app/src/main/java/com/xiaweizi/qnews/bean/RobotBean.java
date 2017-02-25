package com.xiaweizi.qnews.bean;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.bean
 * 类名：    RobotBean
 * 创建者：  夏韦子
 * 创建日期： 2017/2/25
 * 创建时间： 14:45
 */

public class RobotBean {

    /**
     * reason : 成功的返回
     * result : {"code":100000,"text":"你好呀，～有什么新鲜事儿要对我讲？"}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

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

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * code : 100000
         * text : 你好呀，～有什么新鲜事儿要对我讲？
         */

        private int code;
        private String text;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
