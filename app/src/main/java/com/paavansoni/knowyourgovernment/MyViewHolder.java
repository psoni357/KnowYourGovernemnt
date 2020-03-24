package com.paavansoni.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView office;
    TextView name;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        office = itemView.findViewById(R.id.office);
        name = itemView.findViewById(R.id.person);
    }
}
