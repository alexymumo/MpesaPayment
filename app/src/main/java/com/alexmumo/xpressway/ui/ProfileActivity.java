package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.models.Driver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewEmail, textViewPhone, textViewName;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewEmail = findViewById(R.id.driversEmailTextView);
        textViewName = findViewById(R.id.driversNameTextView);
        textViewPhone = findViewById(R.id.phoneTextView);

        // Firebase Auth to get current user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get data from firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Drivers").child(firebaseUser.getUid());

        // Get data and display in a textview
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Driver driver = snapshot.getValue(Driver.class);
                textViewEmail.setText(driver.getEmail());
                textViewName.setText(driver.getFullname());
                textViewPhone.setText(driver.getPhone());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
}