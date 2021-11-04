package com.kospeech.speechteacher;

import android.app.Presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<List<PresentationItem>> getallpresentation();

    @DELETE("presentation/{presentation_id}")
    Call<PresentationViewAdapter.DeletePresentation> deletepresentation(@Path("presentation_id") String presentation_id);

    @Multipart
    @POST("presentationresult/{presentation_id}")
    Call<PresentationResult> presentationresult(@Path("presentation_id") String presentation_id , @Part MultipartBody.Part file, @PartMap HashMap<String,RequestBody> data);

    @GET("presentation/{presentation_id}")
    Call<PresentationItem> getpresentation(@Path("presentation_id") String presentation_id);


    @FormUrlEncoded
    @POST("presentation/")
    Call<PresentationMakeActivity.MakePresentation> makepresentation(@Field("presentation_title") String presentation_title,
                                                                     @Field("presentation_time") String presentation_time,
                                                                     @Field("presentation_date") String presentation_date);

    @Multipart
    @POST("presentationfile/{presentation_id}")
    Call<PresentationMakeActivity.MakePresentationFile> makepresentationfile(@Path("presentation_id") String presentation_id , @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("keyword/{presentation_id}")
    Call<PresentationMakeActivity.MakePresentationECT> makepresentationkeyword(@Path("presentation_id") String presentation_id, @FieldMap Map<String,String> presentationkeyword);

    @FormUrlEncoded
    @POST("script/{presentation_id}")
    Call<PresentationMakeActivity.MakePresentationECT> makepresentationscript(@Path("presentation_id") String presentation_id, @FieldMap Map<String,String> presentationscript);

    @GET("keyword/{presentation_id}")
    Call<List<PresentationPracticeActivity.PresentationKeyword>> getpresentationkeyword(@Path("presentation_id") String presentation_id);

    @GET("script/{presentation_id}")
    Call<List<PresentationPracticeActivity.PresentationScript>> getpresentationscript(@Path("presentation_id") String presentation_id);



    @GET("presentationresult/{presentation_id}")
    Call<List<PresentationResultInfo>> getpresentationresult(@Path("presentation_id") String presentation_id);

    @GET("knowhow/")
    Call<List<KnowhowItem>> getknowhow();


}
