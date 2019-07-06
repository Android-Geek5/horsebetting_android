package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.AccountItemOnclick;
import com.thrillingpicks.views.activities.AccountActivity;
import com.thrillingpicks.views.activities.AddNewCardActivity;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.Myholder> {
    Context context;
    ArrayList<String> list;
    AccountItemOnclick onclick;


    public AccountAdapter(Context context, ArrayList<String> list, AccountItemOnclick onclick) {
        this.context = context;
        this.list = list;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public AccountAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_account_list, viewGroup, false);
        AccountAdapter.Myholder myholder = new AccountAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final AccountAdapter.Myholder myholder, final int postion) {
        //set data in views
        myholder.account_item.setText(list.get(postion));
        //onclick listener
        myholder.account_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.itemClick(v, postion);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        //declare variables
        TextView account_item;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initialize views
            account_item = itemView.findViewById(R.id.account_item);


        }
    }
}

