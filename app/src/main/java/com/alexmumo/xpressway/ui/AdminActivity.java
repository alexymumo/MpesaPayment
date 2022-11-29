package com.alexmumo.xpressway.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.alexmumo.xpressway.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    private CardView complainsCardView, usersCardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        complainsCardView = findViewById(R.id.cardViewComplains);
        usersCardView = findViewById(R.id.cardViewUsers);

        complainsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ComplainsActivity.class));
            }
        });
    }
}