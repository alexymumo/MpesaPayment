package com.alexmumo.xpressway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.alexmumo.xpressway.R;

import com.alexmumo.xpressway.models.Driver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextInputEditText email, phone, fullname, password;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView textViewLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.register_btn);
        email = findViewById(R.id.email_text);
        phone = findViewById(R.id.phone_text);
        fullname = findViewById(R.id.name_text);
        password = findViewById(R.id.password_text);
        textViewLogin = findViewById(R.id.signin_tv);

        firebaseAuth = FirebaseAuth.getInstance();

        // Login TextView
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Fullname = fullname.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String Password = password.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Driver driver = new Driver(Fullname, Email,Phone, Password);
                            FirebaseDatabase.getInstance().getReference("Drivers").child(FirebaseAuth.getInstance().getUid())
                                    .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}