package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.PricingOnClick;
import com.thrillingpicks.model.SubscriptionsBeen;
import com.thrillingpicks.utils.CommonUtils;

import java.text.DecimalFormat;
import java.util.List;

import static com.thrillingpicks.views.activities.PricingActivity.selectedPosition;

public class PricingAdapter extends RecyclerView.Adapter<PricingAdapter.Myholder> {
    Context context;
    List<SubscriptionsBeen.Datum> priceList;
    PricingOnClick pricingOnClick;

    public PricingAdapter(Context context, List<SubscriptionsBeen.Datum> priceList, PricingOnClick pricingOnClick) {
        this.context = context;
        this.priceList = priceList;
        this.pricingOnClick = pricingOnClick;
    }

    @NonNull
    @Override
    public PricingAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate views
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_pricing, viewGroup, false);
        PricingAdapter.Myholder myholder = new PricingAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final PricingAdapter.Myholder myholder, final int i) {
        //set data in views
        try {
            Log.i("PriceingAdapter", ":--" + priceList.get(i).getSubscriptionName());

            myholder.validTv.setText(priceList.get(i).getSubscriptionName());
            if (CommonUtils.isNullOrEmpty(priceList.get(i).getSubscriptionName())) {
                myholder.subscription_interval_tv.setVisibility(View.GONE);
            } else {
                if (priceList.get(i).getSubscriptionValidity() == 0) {
                    myholder.subscription_interval_tv.setVisibility(View.GONE);
                } else {
                    myholder.subscription_interval_tv.setVisibility(View.VISIBLE);
                    myholder.subscription_interval_tv.setText(priceList.get(i).getSubscriptionValidity() + " " + priceList.get(i).getSubscriptionInterval());
                }
            }

            if (priceList.get(i).getSubscriptionType().equals("charge_automatically")) {
                myholder.descTv.setText("Auto Renew");
            } else if (priceList.get(i).getSubscriptionType().equals("send_invoice")) {
                myholder.descTv.setText("One Time Payment");
            } else {
                Log.i("PriceingAdapter", ":--" + priceList.get(i).getSubscriptionType());
                myholder.descTv.setText(priceList.get(i).getSubscriptionType());

            }


            if (!priceList.get(i).getSubscriptionPrice().equals("")) {
                String split[] = priceList.get(i).getSubscriptionPrice().split("\\.");
                String s1 = new DecimalFormat("##.##").format(Double.parseDouble(priceList.get(i).getSubscriptionPrice()));
                myholder.planPrice.setText("$" + s1);
            } else {
                myholder.planPrice.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (selectedPosition == i) {
            GradientDrawable bgShape = (GradientDrawable) myholder.pricing_item_ll.getBackground();
            bgShape.setColor(Color.BLACK);
        } else {
            GradientDrawable bgShape = (GradientDrawable) myholder.pricing_item_ll.getBackground();
            bgShape.setColor(context.getResources().getColor(R.color.loginButtonBg));
        }

        //impelment on click listener on item
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pricingOnClick.onItemClick(myholder.pricing_item_ll, i, priceList.get(i).getId().toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

        private TextView validTv;
        private TextView descTv;
        private TextView planPrice;
        private TextView subscription_interval_tv;
        LinearLayout pricing_item_ll;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initialize views
            validTv =  itemView.findViewById(R.id.valid_tv);
            descTv =  itemView.findViewById(R.id.desc_tv);
            planPrice =  itemView.findViewById(R.id.plan_price);
            subscription_interval_tv =  itemView.findViewById(R.id.subscription_interval_tv);
            pricing_item_ll = itemView.findViewById(R.id.pricing_item_ll);

        }
    }

}

