package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alexmumo.xpressway.R;

import com.alexmumo.xpressway.daraja.Daraja;
import com.alexmumo.xpressway.daraja.DarajaListener;
import com.alexmumo.xpressway.daraja.model.AccessToken;
import com.alexmumo.xpressway.daraja.model.LNMExpress;
import com.alexmumo.xpressway.daraja.model.LNMResult;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;



public class PaymentActivity extends AppCompatActivity{
    private EditText phone;
    private Button pay;
    Daraja daraja;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        phone = findViewById(R.id.phone);
        pay = findViewById(R.id.pay);

        daraja = Daraja.with("kgfUVwVoALb45cnt6t1XmC7oQHHYaftK", "Ju041jMYxbs57OIi", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Toast.makeText(PaymentActivity.this, "TOKEN: " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {


            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phone.getText().toString().trim();

                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                        "1",
                        "254797642381",
                        "174379",
                        phoneNumber,
                        "http://mycallbackurl.com/checkout.php",
                        "001ABC",
                        "xpressway payment"
                );
                daraja.requestMPESAExpress(lnmExpress, new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        Log.i(PaymentActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                    }

                    @Override
                    public void onError(String error) {
                        Log.i(PaymentActivity.this.getClass().getSimpleName(), error);

                    }
                });
            }
        });
    }
}