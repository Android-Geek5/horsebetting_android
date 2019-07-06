package com.thrillingpicks.views.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.thrillingpicks.R;
import com.thrillingpicks.interfaces.CardOnClick;
import com.thrillingpicks.interfaces.CardOnSlide;
import com.thrillingpicks.interfaces.DeleteCardOnClick;
import com.thrillingpicks.model.GetCardBeen;

import java.util.List;

import static com.thrillingpicks.views.activities.CardListActivity.cardOpen;
import static com.thrillingpicks.views.activities.CardListActivity.selected;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.Myholder> {
    Context context;
    DeleteCardOnClick deleteCardOnClick;
    List<GetCardBeen.Datum> cardBeenList;
    CardOnClick cardOnClick;
    String fromactivity;
    CardOnSlide cardOnSlide;
    String TAG = CardListAdapter.class.getSimpleName();

    public CardListAdapter(Context context, List<GetCardBeen.Datum> cardBeenList, String fromactivity, DeleteCardOnClick deleteCardOnClick, CardOnClick cardOnClick, CardOnSlide cardOnSlide) {
        this.context = context;
        this.fromactivity = fromactivity;
        this.deleteCardOnClick = deleteCardOnClick;
        this.cardBeenList = cardBeenList;
        this.cardOnClick = cardOnClick;
        this.cardOnSlide = cardOnSlide;
    }

    @NonNull
    @Override
    public CardListAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View view = LayoutInflater
                .from(viewGroup.getContext()).inflate(R.layout.item_cards, viewGroup, false);
        CardListAdapter.Myholder myholder = new CardListAdapter.Myholder(view);
        return myholder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull final CardListAdapter.Myholder myholder, final int i) {
        try {

            if (fromactivity.equals("PricingActivity")) {
                myholder.card_selected_rl.setVisibility(View.VISIBLE);
                myholder.itemDeleteCard.setVisibility(View.GONE);
                myholder.swipe_layout.setLockDrag(true);
                if (selected == i) {
                    myholder.card_selected_iv.setVisibility(View.VISIBLE);
                    myholder.card_unselected_iv.setVisibility(View.GONE);
                } else {
                    myholder.card_unselected_iv.setVisibility(View.VISIBLE);
                    myholder.card_selected_iv.setVisibility(View.GONE);
                }
            } else {
                myholder.card_selected_rl.setVisibility(View.GONE);
                myholder.itemDeleteCard.setVisibility(View.VISIBLE);
                Log.e(TAG, "check postion" + cardOpen);

                if (cardOpen == i) {

                } else {
                    myholder.swipe_layout.close(true);
                }
            }


            myholder.itemCardNumber.setText("XXXX XXXX XXXX " + cardBeenList.get(i).getCardLastFourNumber());
            myholder.itemCardType.setText(cardBeenList.get(i).getCardPaymentName().toString().toUpperCase());
            myholder.itemExpDate.setText(cardBeenList.get(i).getCardExpMonth() + " / " + cardBeenList.get(i).getCardExpYear().toString().substring(2, 4));
            myholder.itemCardHolderName.setText(cardBeenList.get(i).getName());
            if (cardBeenList.get(i).getCardPaymentName().equals("Visa")) {

                Drawable img = context.getResources().getDrawable(R.drawable.visacard);
                myholder.card_type_img_tv.setImageDrawable(img);

            }
            if (cardBeenList.get(i).getCardPaymentName().equals("MasterCard")) {

                Drawable img = context.getResources().getDrawable(R.drawable.mastercard);
                myholder.card_type_img_tv.setImageDrawable(img);
            }
            if (cardBeenList.get(i).getCardPaymentName().equals("American Express")) {

                Drawable img = context.getResources().getDrawable(R.drawable.americanexpress);
                myholder.card_type_img_tv.setImageDrawable(img);
            }
            if (cardBeenList.get(i).getCardPaymentName().equals("Discover")) {

                Drawable img = context.getResources().getDrawable(R.drawable.discovercard);
                myholder.card_type_img_tv.setImageDrawable(img);
            }

            if (cardBeenList.get(i).getCardPaymentName().equals("Diners Club")) {

                Drawable img = context.getResources().getDrawable(R.drawable.dinnersclubcard);
                myholder.card_type_img_tv.setImageDrawable(img);
            }
            if (cardBeenList.get(i).getCardPaymentName().equals("JCB")) {

                Drawable img = context.getResources().getDrawable(R.drawable.jcbcard);
                myholder.card_type_img_tv.setImageDrawable(img);

            }

            if (cardBeenList.get(i).getCardPaymentName().equals("UnionPay")) {

                Drawable img = context.getResources().getDrawable(R.drawable.unionpay);
                myholder.card_type_img_tv.setImageDrawable(img);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        myholder.itemDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCardOnClick.OnItemclick(v, i, cardBeenList.get(i).getCardId());
            }
        });
        myholder.swipe_layout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
            @Override
            public void onClosed(SwipeRevealLayout view) {
                Log.e(TAG, "onClosed");
            }

            @Override
            public void onOpened(SwipeRevealLayout view) {
                Log.e(TAG, "onOpened");
                cardOnSlide.onCardSlide(i);
            }

            @Override
            public void onSlide(SwipeRevealLayout view, float slideOffset) {
                Log.e(TAG, "onSlide");

            }
        });
        myholder.cardRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOnClick.ItemOnClick(v, i, cardBeenList.get(i).getCardId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return cardBeenList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        private ImageView itemDeleteCard, card_type_img_tv;
        private RelativeLayout cardRl;
        private TextView itemCardType;
        private TextView itemCardHolderName;
        private TextView itemCardNumber;
        private TextView itemExpDate;
        private TextView cvvTv;
        private TextView cvvNumber;
        RelativeLayout card_selected_rl;
        ImageView card_selected_iv;
        ImageView card_unselected_iv;
        SwipeRevealLayout swipe_layout;
        FrameLayout delete_layout;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            //initialize views


            itemDeleteCard = (ImageView) itemView.findViewById(R.id.item_delete_card);
            card_type_img_tv = (ImageView) itemView.findViewById(R.id.card_type_img_tv);

            cardRl = (RelativeLayout) itemView.findViewById(R.id.card_rl);
            itemCardType = (TextView) itemView.findViewById(R.id.item_card_type);
            itemCardHolderName = (TextView) itemView.findViewById(R.id.item_card_holder_name);
            itemCardNumber = (TextView) itemView.findViewById(R.id.item_card_number);
            itemExpDate = (TextView) itemView.findViewById(R.id.item_exp_date);
            cvvTv = (TextView) itemView.findViewById(R.id.cvv_tv);
            cvvNumber = (TextView) itemView.findViewById(R.id.cvv_number);
            card_selected_rl = itemView.findViewById(R.id.card_selected_rl);
            card_selected_iv = itemView.findViewById(R.id.card_selected_iv);
            card_unselected_iv = itemView.findViewById(R.id.card_unselected_iv);
            swipe_layout = itemView.findViewById(R.id.swipe_layout);
            delete_layout = itemView.findViewById(R.id.delete_layout);


        }
    }
}
