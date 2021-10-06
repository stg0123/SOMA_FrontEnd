package com.kospeech.speechteacher;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {
    @GET("/")
    Call<String> HelloWorld();

    @GET("lookup/")
    Call<ResponseBody> getlookup();

    @FormUrlEncoded
    @POST("login/")
    Call<LoginActivity.TokenData> login(@Field("user_email") String user_email, @Field("user_password") String user_password );

}
