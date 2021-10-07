package com.kospeech.speechteacher;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitService {
    @GET("/")
    Call<String> HelloWorld();

    @GET("lookup/")
    Call<ResponseBody> getlookup();

    @FormUrlEncoded
    @POST("login/")
    Call<LoginActivity.TokenData> login(@Field("user_email") String user_email
            , @Field("user_password") String user_password );

    @FormUrlEncoded
    @POST("user/")
    Call<JoinActivity.JoinData> join(@Field("user_email") String user_email
            ,@Field("user_password") String user_password
            ,@Field("user_nickname") String user_nickname);

    @DELETE("user/")
    Call<FragmentMypage.DeleteAccount> deleteaccount();

    @GET("user/")
    Call<FragmentMypage.MyInfo> myinfo();

    @FormUrlEncoded
    @PUT("user/")
    Call<MyinfoUpdateActivity.UpdateData> myinfoupdate(@Field("user_email") String user_email
                                                       ,@Field("user_password") String user_password
                                                       ,@Field("user_nickname") String user_nickname);

}
