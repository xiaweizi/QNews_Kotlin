package com.xiaweizi.qnews.net;


/**
 * 新闻数据回调接口
 */
public interface QNewsCallback<T> {

    /**
     * 数据成功时候回调
     * @param response 成功时候回调的结果
     * @param id        成功码
     */
    void onSuccess(T response, int id);
    /**
     * 数据失败时候回调
     * @param e         失败时回调异常
     * @param id        失败码
     */
    void onError(Exception e, int id);
}
