package com.alexmumo.xpressway.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alexmumo.xpressway.models.AccessToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.alexmumo.xpressway.R;
import static com.alexmumo.xpressway.utils.Constants.PARTYB;
import static com.alexmumo.xpressway.utils.Constants.TRANSACTION_TYPE;
import static com.alexmumo.xpressway.utils.Constants.BUSINESS_SHORT_CODE;
import static com.alexmumo.xpressway.utils.Constants.CALLBACKURL;
import static com.alexmumo.xpressway.utils.Constants.PASSKEY;

import com.alexmumo.xpressway.models.STKPush;
import com.alexmumo.xpressway.network.MpesaApiClient;
import com.alexmumo.xpressway.utils.Utils;

import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.google.android.material.textfield.TextInputEditText;


public class PaymentActivity extends AppCompatActivity {
    private MpesaApiClient mpesaApiClient;
    private ProgressDialog progressDialog;
    TextInputEditText phone, amount;
    Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment();
            }
        });
    }
    private void makePayment() {
        new RavePayManager(this)
                .setAmount(1)
                .setCountry("KE")
                .setCurrency("KES")
                .setEmail("mungai@gmail.com")
                .setfName("Mungai")
                .setlName("Mungai")
                .setNarration("Payment for xpressway")
                .setPublicKey("FLWPUBK_TEST-9e1e9b9e8b9b8e1e8b9b8e1e")
                .setEncryptionKey("FLWSECK_TEST-9e1e9b9e8b9b8e1e8b9b8e1e")
                .setTxRef(System.currentTimeMillis() + "Ref")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(true)
                .onStagingEnv(true)
                .allowSaveCardFeature(true)
                .showStagingLabel(true)
                .initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RavePayManager.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayManager.RESULT_SUCCESS) {
                //Do something with the successful response
            } else if (resultCode == RavePayManager.RESULT_ERROR) {
                //Do something with the error response
            } else if (resultCode == RavePayManager.RESULT_CANCELLED) {
                //Do something when the user cancels the payment
            }
        }
    }
}



        /*
        phone = findViewById(R.id.phone_text);
        amount = findViewById(R.id.amount_text);
        pay = findViewById(R.id.paybutton);
        mpesaApiClient = new MpesaApiClient();
        mpesaApiClient.setIsDebug(true);

        progressDialog = new ProgressDialog(this);
        getAccessToken();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone.getText().toString();
                String amountToPay = amount.getText().toString();
                performStkPush(phoneNumber, amountToPay);
            }
        });
    }
    public void performStkPush(String phoneNumber, String amountToPay) {
        progressDialog.setMessage("Processing Payment");
        progressDialog.setTitle("Please Wait..");
        progressDialog.show();
        String timeStamp = Utils.getTimestamp();
        STKPush  stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE , PASSKEY, timeStamp),
                timeStamp,
                TRANSACTION_TYPE,
                String.valueOf(amountToPay),
                Utils.sanitizePhoneNumber(phoneNumber),
                PARTYB,
                Utils.sanitizePhoneNumber(phoneNumber),
                CALLBACKURL,
                "XpressWay Payment",
                "Test"
        );
        mpesaApiClient.setGetAccessToken(false);
        mpesaApiClient.getSTKPushService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(Call<STKPush> call, Response<STKPush> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<STKPush> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    // methods to get access token and send payment
    public void getAccessToken() {
        mpesaApiClient.setGetAccessToken(true);
        mpesaApiClient.getSTKPushService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    //mpesaApiClient.setAccessToken(response.body().getAccessToken());
                    //mpesaApiClient.setIsGetAccessToken(false);
                    mpesaApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });*/