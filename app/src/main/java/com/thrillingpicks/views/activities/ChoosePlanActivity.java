package com.thrillingpicks.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.thrillingpicks.R;
import com.thrillingpicks.views.adapters.PlanAdapter;

public class ChoosePlanActivity extends AppCompatActivity implements View.OnClickListener {

    Button choose_plan_next_btn;
    ImageView choose_plan_back;
    RecyclerView plan_recycler;
    LinearLayoutManager planlayoutmanager;
    PlanAdapter planAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);
        //initialize views
        initView();
    }

    /*--initialize view---*/
    private void initView() {
        choose_plan_next_btn = findViewById(R.id.choose_plan_next_btn);
        choose_plan_back = findViewById(R.id.choose_plan_back);
        plan_recycler = findViewById(R.id.plan_recycler);
        //call setadapter method
        setAdapter();
        //call clickviews method
        clickView();
    }

    /*set recylerview*/
    private void setAdapter() {
        planlayoutmanager = new LinearLayoutManager(this, 0, false);
        plan_recycler.setLayoutManager(planlayoutmanager);
        planAdapter = new PlanAdapter(this);
        plan_recycler.setAdapter(planAdapter);

    }

    /*--clickable view---*/
    private void clickView() {
        choose_plan_next_btn.setOnClickListener(this);
        choose_plan_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_plan_next_btn:
                //go to add new card activity
                startActivity(new Intent(this, AddNewCardActivity.class));
                break;
            case R.id.choose_plan_back:
                //back press functionality
                onBackPressed();
                break;
        }

    }

    /*--back press--*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
