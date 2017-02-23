package com.xiaweizi.qnews.net;

import com.xiaweizi.qnews.bean.JokeBean;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface QNewsService {

    @POST("text.from?key=ae240f7fba620fc370b803566654949e")
    Observable<JokeBean> getCurrentJokeData(
            @Query("page") int page,
            @Query("pagesize") int pagesize
    );
}
