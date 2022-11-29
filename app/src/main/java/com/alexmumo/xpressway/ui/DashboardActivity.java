package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.ui.LogbookActivity;
import com.alexmumo.xpressway.models.Driver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {
    private CardView cardViewProfile, cardViewTrip, cardViewHistory, cardViewLocation,logBookCardView, logoutCardView, cardViewComplains;
    private TextView usernameTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get data from firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers").child(firebaseUser.getUid());
        logBookCardView = findViewById(R.id.logBookCardView);
        cardViewLocation = findViewById(R.id.locationCardView);
        cardViewTrip = findViewById(R.id.cardViewPay);
        logoutCardView = findViewById(R.id.cardViewLogout);
        cardViewProfile = findViewById(R.id.cardView2);
        cardViewComplains = findViewById(R.id.cardViewComplains);

        cardViewComplains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ReportComplainActivity.class));
            }
        });

        // Logout cardview
        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                Toast.makeText(DashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                finish();
            }
        });

        logBookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,LogbookActivity.class);
                startActivity(intent);
            }
        });
        cardViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DistanceActivity.class);
                startActivity(intent);
            }
        });

        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // listener for pay cardview
        cardViewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, TripActivity.class));
            }
        });
    }
}