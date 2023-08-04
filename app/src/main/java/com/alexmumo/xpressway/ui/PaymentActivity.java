package com.alexmumo.xpressway.ui;

import static com.alexmumo.xpressway.utils.Constants.CONSUMER_KEY;
import static com.alexmumo.xpressway.utils.Constants.CONSUMER_SECRET;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alexmumo.xpressway.daraja.Daraja;
import com.alexmumo.xpressway.daraja.DarajaListener;
import com.alexmumo.xpressway.daraja.model.AccessToken;
import com.alexmumo.xpressway.daraja.model.LNMExpress;
import com.alexmumo.xpressway.daraja.model.LNMResult;
import com.alexmumo.xpressway.databinding.ActivityPaymentBinding;


public class PaymentActivity extends AppCompatActivity{
    private ActivityPaymentBinding binding;
    Daraja daraja;
    String phoneNumber;
    String amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        daraja = Daraja.with(CONSUMER_KEY, CONSUMER_SECRET, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                //Toast.makeText(PaymentActivity.this, "TOKEN: " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PaymentActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        binding.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = binding.phone.getText().toString();
                amount = binding.amount.getText().toString();

                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                        amount,
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