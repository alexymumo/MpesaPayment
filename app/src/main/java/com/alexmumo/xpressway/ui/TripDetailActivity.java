package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.models.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TripDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button paymentBtn;
    TextView amount, exit, entrance, vehicleClass, distance, vehicleDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        amount = findViewById(R.id.tvAmount);
        exit = findViewById(R.id.tvExit);
        entrance = findViewById(R.id.tvEntrance);
        vehicleClass = findViewById(R.id.tvClass);
        vehicleDefinition = findViewById(R.id.tvDefinition);
        distance = findViewById(R.id.tvDistance);
        paymentBtn = findViewById(R.id.paymentBtn);



        Intent intent = getIntent();
        String distanceStr = intent.getStringExtra("distance");
        distance.setText(distanceStr);
        String amountStr = intent.getStringExtra("amount");
        amount.setText(amountStr);

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailActivity.this, PaymentActivity.class);
                intent.putExtra("amount",amountStr);
                startActivity(intent);
            }
        });

        /*paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripDetailActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

         */
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Trips").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Trip trip = snapshot.getValue(Trip.class);
                exit.setText(trip.getExit());
                entrance.setText(trip.getEntrance());
                vehicleClass.setText(trip.getVehicle_class());
                vehicleDefinition.setText(trip.getVehicle_definition());
                //double distanceInKm = Double.parseDouble(distance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

