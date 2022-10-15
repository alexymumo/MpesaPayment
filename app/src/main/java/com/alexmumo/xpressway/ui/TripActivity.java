package com.alexmumo.xpressway.ui;

import com.alexmumo.xpressway.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.alexmumo.xpressway.models.Trip;
import com.google.firebase.database.ValueEventListener;

public class TripActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextInputEditText vehicleClass, vehicleDefinition, exit, entrance;
    private Spinner spinner_exit, spinner_entrance, spinner_definition, spinner_class;
    private Button tripButton;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        // Database Reference
        // databaseReference = FirebaseDatabase.getInstance().getReference("Trips");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Trips");

        // initialize empty constructor
        trip = new Trip();

        // TextInputEditText
        entrance = findViewById(R.id.entrance_text);
        exit = findViewById(R.id.exit_text);
        vehicleClass = findViewById(R.id.vehicle_text);
        vehicleDefinition = findViewById(R.id.vehicle_definition_text);

        // Spinners
        spinner_exit = findViewById(R.id.exitSpinner);
        spinner_entrance = findViewById(R.id.entrance_spinner);
        spinner_class = findViewById(R.id.vehicleSpinner);
        spinner_definition = findViewById(R.id.spinnerDefinition);


        // Button
        tripButton = findViewById(R.id.submitTripBtn);

        // Vehicle Definition Adapter
        ArrayAdapter vehicleDefAdapter = ArrayAdapter.createFromResource(this, R.array.vehicle_definition, android.R.layout.simple_spinner_dropdown_item);
        vehicleDefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_definition.setAdapter(vehicleDefAdapter);
        spinner_definition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String definition = parent.getItemAtPosition(position).toString();
                vehicleDefinition.setText(definition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Vehicle Class adapter
        ArrayAdapter vehicleClassAdapter = ArrayAdapter.createFromResource(this, R.array.vehicle_class, android.R.layout.simple_spinner_dropdown_item);
        vehicleClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(vehicleClassAdapter);
        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String vehicle = parent.getItemAtPosition(position).toString();
                vehicleClass.setText(vehicle);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // entrance adapter
        ArrayAdapter entranceAdapter = ArrayAdapter.createFromResource(this, R.array.entrance_array, android.R.layout.simple_spinner_dropdown_item);
        entranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_entrance.setAdapter(entranceAdapter);
        spinner_entrance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                entrance.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(TripActivity.this, "Please select an entrance", Toast.LENGTH_SHORT).show();
            }
        });

        // exit adapter
        ArrayAdapter exitAdapter = ArrayAdapter.createFromResource(this, R.array.exit_array, android.R.layout.simple_spinner_dropdown_item);
        exitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_exit.setAdapter(exitAdapter);
        spinner_exit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String exitText = parent.getItemAtPosition(position).toString();
                exit.setText(exitText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // submit trip button
        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entrance_text = entrance.getText().toString();
                String exit_text = exit.getText().toString();
                String class_text = vehicleClass.getText().toString();
                String definition_text = vehicleDefinition.getText().toString();

                trip.setVehicle_class(class_text);
                trip.setVehicle_definition(definition_text);
                trip.setEntrance(entrance_text);
                trip.setExit(exit_text);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
                    String tripId = firebaseUser.getUid();

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child(tripId).setValue(trip);
                        Toast.makeText(TripActivity.this, "Trip Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TripActivity.this, TripDetailActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TripActivity.this, "Trip not Saved", Toast.LENGTH_SHORT).show();

                    }
                });

                //Intent intent = new Intent(TripActivity.this, PaymentActivity.class);
                //startActivity(intent);
            }
        });

    }
}