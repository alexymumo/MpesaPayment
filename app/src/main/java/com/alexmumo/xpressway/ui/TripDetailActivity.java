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

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class TripDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button paymentBtn;
    private TextView amount, exit, entrance, vehicleClass, distance, vehicleDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        amount = findViewById(R.id.tvAmount);
        exit = findViewById(R.id.tvExit);
        entrance = findViewById(R.id.tvEntrance);
        vehicleClass = findViewById(R.id.tvClass);
        vehicleDefinition = findViewById(R.id.tvDefinition);


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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

