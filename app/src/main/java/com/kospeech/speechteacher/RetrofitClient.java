package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL = "https://kospeech.com/";
    public RetrofitClient(){

        //토큰 만료 기능 추가시 체크 url
        //https://kimch3617.tistory.com/entry/Retrofit%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%86%A0%ED%81%B0-%EC%9D%B8%EC%A6%9D-%EB%B0%A9%EC%8B%9D-%EA%B5%AC%ED%98%84

    }
    public static Retrofit getClient(String token){
        OkHttpClient.Builder clientbuilder = new OkHttpClient.Builder();
        clientbuilder.connectTimeout(5, TimeUnit.MINUTES);
        clientbuilder.readTimeout(5,TimeUnit.MINUTES);
        clientbuilder.writeTimeout(1,TimeUnit.MINUTES);

        clientbuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization",token)
                        .build();
                Log.d(TAG, "intercept: addheader"+token);
                return chain.proceed(request);
            }
        });

        OkHttpClient client = clientbuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


}
