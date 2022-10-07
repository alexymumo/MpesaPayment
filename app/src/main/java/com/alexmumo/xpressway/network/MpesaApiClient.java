package com.alexmumo.xpressway.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.alexmumo.xpressway.payment.AuthInterceptor;
import com.alexmumo.xpressway.payment.AccessTokenInterceptor;
import com.alexmumo.xpressway.utils.Constants;
import com.alexmumo.xpressway.network.STKPushService;

import static com.alexmumo.xpressway.utils.Constants.BASE_URL;
import static com.alexmumo.xpressway.utils.Constants.CONNECT_TIMEOUT;
import static com.alexmumo.xpressway.utils.Constants.READ_TIMEOUT;
import static com.alexmumo.xpressway.utils.Constants.WRITE_TIMEOUT;


public class MpesaApiClient {
    private Retrofit retrofit;
    private boolean isDebug;
    private boolean isGetAccessToken;
    private String mAuthToken;

    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    public MpesaApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public MpesaApiClient setAuthToken(String authToken) {
        mAuthToken = authToken;
        return this;
    }

    public MpesaApiClient setGetAccessToken(boolean getAccessToken) {
        isGetAccessToken = getAccessToken;
        return this;
    }

    private OkHttpClient.Builder okHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        return okHttpClient;
    }

    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());

        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder okhttpBuilder = okHttpClient();

        if (isGetAccessToken) {
            okhttpBuilder.addInterceptor(new AccessTokenInterceptor());
        }

        if (mAuthToken != null && !mAuthToken.isEmpty()) {
            okhttpBuilder.addInterceptor(new AuthInterceptor(mAuthToken));
        }

        builder.client(okhttpBuilder.build());

        retrofit = builder.build();

        return retrofit;
    }
    public STKPushService getSTKPushService() {
        return getRestAdapter().create(STKPushService.class);
    }
}
