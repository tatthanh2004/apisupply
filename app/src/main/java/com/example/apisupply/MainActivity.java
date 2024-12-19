package com.example.apisupply;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apisupply.Supply;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter<Supply, SupplyViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        db.enableNetwork();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("CarSupply").orderBy("supplyId");

        FirestoreRecyclerOptions<Supply> options = new FirestoreRecyclerOptions.Builder<Supply>()
                .setQuery(query, Supply.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Supply, SupplyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SupplyViewHolder holder, int position, @NonNull Supply model) {
                holder.tvName.setText(model.getSupplyName());
                holder.tvPrice.setText("Price: " + model.getPrice());
            }

            @NonNull
            @Override
            public SupplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.supply_item, parent, false);
                return new SupplyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //adapter.notifyDataSetChanged();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
