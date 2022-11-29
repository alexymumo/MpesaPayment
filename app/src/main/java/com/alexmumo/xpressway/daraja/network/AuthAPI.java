package com.alexmumo.xpressway.daraja.network;

import com.alexmumo.xpressway.daraja.model.AccessToken;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthAPI {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessToken> getAccessToken();
}
