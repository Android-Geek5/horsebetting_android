package com.thrillingpicks.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.ApplyPromoItemOnclick;
import com.thrillingpicks.views.adapters.ApplyPromoCodeAdapter;
import com.thrillingpicks.views.adapters.PlanAdapter;

public class ApplyPromoCodeActivity extends AppCompatActivity implements View.OnClickListener {
    static String TAG = ApplyPromoCodeActivity.class.getSimpleName();
    private LinearLayout promoBack;
    private ImageView addNewCardBack;
    private TextView rewardsTitleTv;
    private RecyclerView rewardsRecyler;
    private RelativeLayout promoRl;
    private TextView rewardsPriceTv;
    private TextView rewardsApplyTv;
    LinearLayoutManager linearLayoutManager;
    ApplyPromoCodeAdapter applyPromoCodeAdapter;
    public static int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_promo_code);
        //initialize view
        initView();
    }

    /*initialize views*/
    private void initView() {
        promoBack =  findViewById(R.id.promo_back);
        addNewCardBack =  findViewById(R.id.add_new_card_back);
        rewardsTitleTv =  findViewById(R.id.rewards_title_tv);
        rewardsRecyler =  findViewById(R.id.rewards_recyler);
        promoRl =  findViewById(R.id.promo_rl);
        rewardsPriceTv =  findViewById(R.id.rewards_price_tv);
        rewardsApplyTv =  findViewById(R.id.rewards_apply_tv);
        //initialize clickable views
        clickView();
        //set adapter in recyclerview
        setAdapter();
    }

    /*load recylerview*/
    private void setAdapter() {
        linearLayoutManager = new LinearLayoutManager(this, 0, false);
        rewardsRecyler.setLayoutManager(linearLayoutManager);
        applyPromoCodeAdapter = new ApplyPromoCodeAdapter(this, new ApplyPromoItemOnclick() {
            @Override
            public void promoOnClick(View view, int position) {
                Log.e(TAG, "check promo click:--" + position);
                promoRl.setVisibility(View.VISIBLE);
                selected = position;
                applyPromoCodeAdapter.notifyDataSetChanged();
            }
        });
        rewardsRecyler.setAdapter(applyPromoCodeAdapter);
    }

    /*initialize clickable views*/
    private void clickView() {
        promoBack.setOnClickListener(this);
        promoBack.setOnClickListener(this);
        promoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
