package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.alexmumo.xpressway.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alexmumo.xpressway.models.Complains;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportComplainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextInputEditText email, type, description;
    private FirebaseAuth firebaseAuth;
    private Button complainBtn;
    Complains complains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_complain);

        complains = new Complains();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Complains");

        complainBtn = findViewById(R.id.reportBtn);
        email = findViewById(R.id.email_text_complains);
        type = findViewById(R.id.type_text_complains);
        description = findViewById(R.id.type_text_complains);

        complainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text = email.getText().toString();
                String type_text = type.getText().toString();
                String description_text = description.getText().toString();

                complains.setMessage(description_text);
                complains.setType(type_text);
                complains.setName(email_text);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        FirebaseUser firebaseUser =  firebaseAuth.getCurrentUser();
                        String complainId = firebaseUser.getUid();
                        databaseReference.child(complainId).setValue(complains);
                        Toast.makeText(ReportComplainActivity.this, "Complain Posted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ReportComplainActivity.this, DashboardActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(ReportComplainActivity.this, "Failed to post data", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}