package com.kospeech.speechteacher;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

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

    @GET("presentation/")
    Call<List<FragmentPresentation.PresentationItem>> getpresentation();

    @Multipart
    @POST("presentationresulttest/")
    Call<PresentationResult> presentationresult(@Part MultipartBody.Part file);

}
