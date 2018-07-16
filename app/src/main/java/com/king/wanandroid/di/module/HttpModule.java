package com.king.wanandroid.di.module;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.king.wanandroid.api.ApiService;
import com.king.wanandroid.api.LogInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Module
public class HttpModule {

    private static final int DEFAULT_TIME_OUT = 15;

    private String baseUrl;


    public HttpModule(String baseUrl){
        this.baseUrl = baseUrl;
    }


    @Singleton
    @Provides
    public PersistentCookieJar provideCookieJar(Context context){
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(PersistentCookieJar cookieJar){
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
                .cookieJar(cookieJar)
                .build();

    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient client){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Singleton
    @Provides
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);

    }



}
