package com.paavansoni.knowyourgovernment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PoliticianAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "PoliticianAdapter";
    private List<Politician> politicianList;
    private MainActivity mainAct;

    PoliticianAdapter(List<Politician> empList, MainActivity ma){
        this.mainAct = ma;
        this.politicianList = empList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.politicial_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER " + position);

        Politician p = politicianList.get(position);

        holder.office.setText(p.getOffice());
        String person = p.getName() + " (" + p.getParty() + ")";
        holder.name.setText(person);
    }

    @Override
    public int getItemCount() {
        return politicianList.size();
    }
}
