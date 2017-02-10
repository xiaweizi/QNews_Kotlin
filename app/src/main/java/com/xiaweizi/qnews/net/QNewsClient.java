package com.xiaweizi.qnews.net;

import com.google.gson.Gson;
import com.xiaweizi.qnews.bean.NewsDataBean;
import com.xiaweizi.qnews.bean.TodayOfHistoryBean;
import com.xiaweizi.qnews.bean.TodayOfHistoryDetailBean;
import com.xiaweizi.qnews.commons.Constants;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 工程名：  QNews
 * 包名：    com.xiaweizi.qnews.net
 * 类名：    QNewsClient
 * 创建者：  夏韦子
 * 创建日期： 2017/2/9
 * 创建时间： 10:42
 */

public class QNewsClient {

    private QNewsCallback mQNewsCallback;

    //单例模式
    private static QNewsClient mQNewsClient;

    private Gson mGson;

    private QNewsClient(){
        mGson = new Gson();
    }

    public static QNewsClient getInstance() {
        if (mQNewsClient == null){
            synchronized (QNewsClient.class){
                if (mQNewsClient == null){
                    mQNewsClient = new QNewsClient();
                }
            }
        }
        return mQNewsClient;
    }

    /**
     * 根据相应的新闻类型获取新闻数据
     * @param type       新闻的类型
     * @param callback   数据的回调接口
     */
    public void GetNewsData(String type, QNewsCallback callback) {

        mQNewsCallback = callback;

        OkHttpUtils.post()
                .url(Constants.NEWS_DATA_URL)
                .addParams("type", type)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mQNewsCallback.onError(e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        NewsDataBean newsDataBean = mGson.fromJson(response, NewsDataBean.class);
                        mQNewsCallback.onSuccess(newsDataBean, id);
                    }
                });

    }

    /**
     * 根据用户输入的信息，进行回答
     * @param info      用户输入的信息
     * @param callback  数据的回调接口
     */
    public void GetQARobotData(String info, QNewsCallback callback){
        mQNewsCallback = callback;

        OkHttpUtils.post()
                .url(Constants.Q_A_ROBOT_URL)
                .addParams("info", info)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mQNewsCallback.onError(e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject result = jsonObject.getJSONObject("result");
                            String text = result.getString("text");
                            mQNewsCallback.onSuccess(text, id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 根据年月，获取 历史上的今天 的概述
     * @param month         查询的c月
     * @param day           查询的日
     * @param callback      查询回调的接口
     */
    public void GetTodayOfHistoryData(int month, int day, QNewsCallback callback){
        mQNewsCallback = callback;

        String params = month + "/" + day;

        OkHttpUtils.post()
                .url(Constants.TODAY_OF_HISTORY_URP)
                .addParams("date", params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mQNewsCallback.onError(e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TodayOfHistoryBean todayOfHistoryBean = mGson.fromJson(response, TodayOfHistoryBean.class);
                        mQNewsCallback.onSuccess(todayOfHistoryBean, id);
                    }
                });
    }

    /**
     * 根据上一个方法成功查询后回调结果中的e_id，获取详细数据
     * @param e_id          上一个方法成功查询后回调结果中的e_id
     * @param callback      查询回调的接口
     */
    public void GetTodayOfHistoryDetailData(int e_id, QNewsCallback callback){

        mQNewsCallback = callback;

        OkHttpUtils.post()
                .url(Constants.TODAY_OF_HISTORY_DETAIL_URL)
                .addParams("e_id", e_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mQNewsCallback.onError(e, id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TodayOfHistoryDetailBean bean = mGson.fromJson(response, TodayOfHistoryDetailBean.class);
                        mQNewsCallback.onSuccess(bean, id);
                    }
                });
    }



}
