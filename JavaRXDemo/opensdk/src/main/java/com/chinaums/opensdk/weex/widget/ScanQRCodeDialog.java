package com.chinaums.opensdk.weex.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.covics.zxingscanner.OnDecodeCompletionListener;
import com.covics.zxingscanner.ScannerView;

/**
 * @Created at :  2017/6/15.
 * @Developer :  JiangBo
 * @Version :  1.0
 * @Description :
 */

public class ScanQRCodeDialog extends Dialog implements OnDecodeCompletionListener {
    private OnDecodeCompletionListener listener;
    private ScannerView scanner;

    public ScanQRCodeDialog(Context context, OnDecodeCompletionListener listener) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        setOwnerActivity((Activity) context);
        this.listener = listener;
        init();
    }

    private void init() {
        scanner = new ScannerView(getContext(), null);
        scanner.setBackgroundColor(Color.BLACK);
        scanner.setOnDecodeListener(this);
        setContentView(scanner);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanner.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanner.onPause();
    }

    @Override
    public void onDecodeCompletion(String barcodeFormat, String barcode, Bitmap bitmap) {
        listener.onDecodeCompletion(barcodeFormat, barcode, bitmap);
                dismiss();
    }
}
