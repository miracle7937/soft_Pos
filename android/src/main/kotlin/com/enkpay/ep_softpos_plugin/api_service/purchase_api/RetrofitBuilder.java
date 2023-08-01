package com.enkpay.ep_softpos_plugin.api_service.purchase_api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    final String EPCashOutPoint= "https://testpos.enkpay.com/api/";

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    final Retrofit.Builder   baseBuilder= new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                    getBaseOkhttpClientBuilder()
                            .build());




    private  OkHttpClient.Builder getBaseOkhttpClientBuilder() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
                .callTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES);
        return okHttpClientBuilder;
    }


    public  PurchaseService isFundUserWallet(){
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(EPCashOutPoint)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(PurchaseService.class);
    }
}
