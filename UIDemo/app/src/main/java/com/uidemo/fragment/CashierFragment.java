package com.uidemo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uidemo.R;

/**
 * Created by Administrator on 2016/10/18.
 */

public class CashierFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout addBtn;
    private LinearLayout cardActionBtn;
    private LinearLayout scanActionBtn;
    private LinearLayout qrCodeActionBtn;
    private ImageView showOrHiddenAmount;
    private TextView acquireCountAmountTv, acquireCountAmountBeforeTv, acquireCountAmountAfterTv;
    private TextView acquireCountTv;
    private LinearLayout countLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashier,null);
        setupView(view);
        return view;
    }

    private void setupView(View view){
        addBtn = (LinearLayout) view.findViewById(R.id.key_add);
        cardActionBtn = (LinearLayout) view.findViewById(R.id.card);
        scanActionBtn = (LinearLayout) view.findViewById(R.id.scan);
        qrCodeActionBtn = (LinearLayout) view.findViewById(R.id.qrcode);
        showOrHiddenAmount = (ImageView) view.findViewById(R.id.cashier_amount_show_hidden);

        acquireCountAmountTv = (TextView) view.findViewById(R.id.cashier_acquire_count_amount_tv);
        acquireCountAmountAfterTv = (TextView) view.findViewById(R.id.cashier_acquire_count_amount_after_tv);
        acquireCountAmountBeforeTv = (TextView) view.findViewById(R.id.cashier_acquire_count_amount_before_tv);
        acquireCountTv = (TextView) view.findViewById(R.id.cashier_acquire_count_tv);
        countLayout = (LinearLayout) view.findViewById(R.id.cashier_count_layout);
        acquireCountAmountTv.setText("250000000000.25");
        countLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                countLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int layoutWidth = countLayout.getWidth();
                int beforeTvWidth = acquireCountAmountBeforeTv.getWidth();
                int afterTvWidth = acquireCountAmountAfterTv.getWidth();
                acquireCountAmountTv.setMaxWidth(layoutWidth-beforeTvWidth-afterTvWidth-100);
                acquireCountTv.setMaxWidth(layoutWidth-beforeTvWidth-afterTvWidth);
            }
        });


        addBtn.setOnClickListener(this);
        cardActionBtn.setOnClickListener(this);
        scanActionBtn.setOnClickListener(this);
        qrCodeActionBtn.setOnClickListener(this);
        showOrHiddenAmount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card:break;
            case R.id.qrcode:break;
            case R.id.scan:break;
            case R.id.key_add:break;
            case R.id.cashier_amount_show_hidden:
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
