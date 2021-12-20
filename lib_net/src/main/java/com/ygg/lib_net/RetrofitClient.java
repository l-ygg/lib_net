package com.ygg.lib_net;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright (C) 2021 重庆呼我出行网络科技有限公司
 * 版权所有
 * <p>
 * 功能描述：
 * <p>
 * <p>
 * 作者：lengyang 2021/12/20
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
public class RetrofitClient {

    //超时时间
    private int DEFAULT_TIMEOUT = 20;
    //服务端根路径
    private String baseUrl = "";
    private OkHttpClient okHttpClient;
    private OkHttpClient.Builder builder;
    private static Retrofit retrofit;
    private static RetrofitClient INSTANCE;

    public static RetrofitClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitClient();
        }
        return INSTANCE;
    }

    public void init() {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException("请先设置服务器地址");
        }
        builder = new OkHttpClient.Builder();
        okHttpClient = builder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 添加网络日志
     *
     * @param isAdd
     */
    public void addHttpLog(boolean isAdd) {
        if (isAdd) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("lib_net");
            interceptor.setColorLevel(Level.INFO);
            interceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
    }

    /**
     * 添加全局请求头
     *
     * @param map
     */
    public void addHeaderPrams(Map<String, String> map) {
        builder.addInterceptor(new HeaderInterceptor(map));
    }

    /**
     * 添加全局公共参数
     *
     * @param map
     */
    public void addContentPrams(Map<String, String> map) {
        builder.addInterceptor(new PramsInterceptor(map));
    }


    /**
     * 添加拦截器
     *
     * @param interceptor
     */
    public void addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
    }

    /**
     * 设置超时时间
     *
     * @param time
     */
    public void setDefaultTimeOut(int time) {
        this.DEFAULT_TIMEOUT = time;
    }

    /**
     * 获取超时时间，秒
     *
     * @return
     */
    public int getDefaultTimeOut() {
        return DEFAULT_TIMEOUT;
    }

    /**
     * 设置服务器路径
     *
     * @param url
     */
    public void setBaseUrl(String url) {
        this.baseUrl = url;
    }

    /**
     * 或者服务器地址
     *
     * @return
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
}
