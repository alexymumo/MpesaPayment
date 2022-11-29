package com.alexmumo.xpressway.ui;
import com.alexmumo.xpressway.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexmumo.xpressway.R;
import com.alexmumo.xpressway.adapters.ComplainsAdapter;
import com.alexmumo.xpressway.models.Complains;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ComplainsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    List<Complains> complainsList;
    private ComplainsAdapter complainsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complains);

        recyclerView = findViewById(R.id.complainsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        complainsList = new ArrayList<>();
        complainsAdapter = new ComplainsAdapter(this, complainsList);
        recyclerView.setAdapter(complainsAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("Complains");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Complains complains = dataSnapshot.getValue(Complains.class);
                    complainsList.add(complains);
                }
                complainsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ComplainsActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}