package com.alexmumo.xpressway.payment;
import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private String authToken;

    public AuthInterceptor(String authToken) {
         this.authToken = authToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","Bearer n3GrUAwpwxIfyjjgE8mAh83xmvxc" + authToken)
                .build();
        return chain.proceed(authenticatedRequest);
    }
}

