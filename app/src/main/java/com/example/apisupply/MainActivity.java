package com.example.apisupply;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
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
                holder.itemView.setOnClickListener(v -> showCrudDialog(model));
            }

            @NonNull
            @Override
            public SupplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supply_item, parent, false);
                return new SupplyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

        Button btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> showCrudDialog(null));
    }

    private void showCrudDialog(Supply supply) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_crud, null, false);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText etId = view.findViewById(R.id.etId);
        EditText etName = view.findViewById(R.id.etName);
        EditText etPrice = view.findViewById(R.id.etPrice);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);

        if (supply != null) {
            etId.setText(String.valueOf(supply.getSupplyId()));
            etName.setText(supply.getSupplyName());
            etPrice.setText(String.valueOf(supply.getPrice()));
        }

        btnSubmit.setOnClickListener(v -> {
            String id = etId.getText().toString();
            String name = etName.getText().toString();
            String price = etPrice.getText().toString();

            if (id.isEmpty() || name.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (supply == null) {
                // Add new supply
                db.collection("CarSupply").document(id)
                        .set(new Supply(Integer.parseInt(id), name, Integer.parseInt(price)))
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show());
            } else {
                // Update existing supply
                db.collection("CarSupply").document(String.valueOf(supply.getSupplyId()))
                        .update("supplyName", name, "price", Integer.parseInt(price))
                        .addOnSuccessListener(aVoid -> Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show());
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private static class SupplyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        SupplyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }


}
