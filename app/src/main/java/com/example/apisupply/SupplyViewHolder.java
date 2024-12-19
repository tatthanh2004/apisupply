package com.example.apisupply;


import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SupplyViewHolder extends RecyclerView.ViewHolder {
    TextView tvName, tvPrice;

    public SupplyViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tvName);
        tvPrice = itemView.findViewById(R.id.tvPrice);
    }
}
