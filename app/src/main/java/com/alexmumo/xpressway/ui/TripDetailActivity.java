package com.alexmumo.xpressway.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.alexmumo.xpressway.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import android.os.Bundle;

public class TripDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
    }
}