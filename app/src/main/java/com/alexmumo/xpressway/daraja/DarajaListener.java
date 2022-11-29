package com.alexmumo.xpressway.daraja;

//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;

public interface DarajaListener<Result> {
    void onResult(@NonNull Result result);

    void onError(String error);
}
