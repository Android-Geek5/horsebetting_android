package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.ApplyPromoItemOnclick;

import static com.thrillingpicks.views.activities.ApplyPromoCodeActivity.selected;


public class ApplyPromoCodeAdapter extends RecyclerView.Adapter<ApplyPromoCodeAdapter.Myholder> {
    Context context;
    ApplyPromoItemOnclick promoItemOnclick;

    public ApplyPromoCodeAdapter(Context context, ApplyPromoItemOnclick promoItemOnclick) {
        this.context = context;
        this.promoItemOnclick = promoItemOnclick;
    }

    @NonNull
    @Override
    public ApplyPromoCodeAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_choose_plan, viewGroup, false);
        ApplyPromoCodeAdapter.Myholder myholder = new ApplyPromoCodeAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final ApplyPromoCodeAdapter.Myholder myholder, final int i) {
        //set data in views
        Log.e("check_position_onclick", "" + selected + "--" + i);
        if (selected == i) {
            myholder.reward_select.setVisibility(View.VISIBLE);
            myholder.reward_unselect.setVisibility(View.GONE);
        } else {
            myholder.reward_unselect.setVisibility(View.VISIBLE);
            myholder.reward_select.setVisibility(View.GONE);
        }
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoItemOnclick.promoOnClick(v, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class Myholder extends RecyclerView.ViewHolder {

        ImageView reward_select, reward_unselect;
        TextView refer_name, refer_discount_tv;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initalize views
            reward_select = itemView.findViewById(R.id.reward_select);
            reward_unselect = itemView.findViewById(R.id.reward_unselect);
            refer_name = itemView.findViewById(R.id.refer_name);
            refer_discount_tv = itemView.findViewById(R.id.refer_discount_tv);
        }
    }
}

