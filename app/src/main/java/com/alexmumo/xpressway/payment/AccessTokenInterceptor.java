package com.alexmumo.xpressway.payment;

import android.util.Base64;
import java.io.IOException;
import androidx.annotation.NonNull;

import com.alexmumo.xpressway.BuildConfig;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AccessTokenInterceptor implements Interceptor {

    public AccessTokenInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .addHeader("Authorization", "Basic" + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(newRequest);
    }
}
