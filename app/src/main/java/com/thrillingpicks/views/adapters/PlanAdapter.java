package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thrillingpicks.R;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.Myholder> {
    Context context;

    public PlanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PlanAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_choose_plan, viewGroup, false);
        PlanAdapter.Myholder myholder = new PlanAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final PlanAdapter.Myholder myholder, int i) {
   //set data in views
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class Myholder extends RecyclerView.ViewHolder {


        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initalize views
        }
    }
}

