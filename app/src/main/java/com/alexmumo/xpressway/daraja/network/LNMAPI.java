package com.alexmumo.xpressway.daraja.network;

import com.alexmumo.xpressway.daraja.model.LNMResult;
import com.alexmumo.xpressway.daraja.model.LNMExpress;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LNMAPI {
    @POST("mpesa/stkpush/v1/processrequest")
    Call<LNMResult> getLNMPesa(@Body LNMExpress lnmExpress);
}
