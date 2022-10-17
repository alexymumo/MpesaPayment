package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.distance.DistanceFragment;
import com.alexmumo.xpressway.models.Driver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity {
    private CardView cardViewProfile, cardViewTrip, cardViewHistory, cardViewLocation;
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


        // usernameTextView = findViewById(R.id.tvUsername);

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Driver driver = snapshot.getValue(Driver.class);
                usernameTextView.setText(driver.getFullname());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */

        cardViewLocation = findViewById(R.id.locationCardView);
        cardViewTrip = findViewById(R.id.cardViewPay);

        // Profile CardView
        cardViewProfile = findViewById(R.id.cardView2);

        cardViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DistanceFragment.class);
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