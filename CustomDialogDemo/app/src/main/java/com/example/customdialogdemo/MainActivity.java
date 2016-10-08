package com.example.customdialogdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {


    private CustomDialog mTwoButtonDialog;
    private CustomDialog mNoButtonDialog;
    private CustomDialog mOneButtonDialog;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoButtonDialog = new CustomDialog(this, CustomDialog.BUTTON_TYPE_TWO, new ButtonSetting(Color.parseColor("#B8B8B8"), "忘记密码", this)
                , new ButtonSetting(Color.parseColor("#55659C"), "重新输入", this));
        mNoButtonDialog = new CustomDialog(this,CustomDialog.BUTTON_TYPE_NO,null,null);
        mOneButtonDialog = new CustomDialog(this, CustomDialog.BUTTON_TYPE_SINGLE, new ButtonSetting(Color.parseColor("#A6D93E"), "确定", new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mOneButtonDialog.dismiss();
                Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
            }
        })  , null);
        findViewById(R.id.showNoButtonDialogBtn).setOnClickListener(this);
        findViewById(R.id.showOneButtonDialogBtn).setOnClickListener(this);
        findViewById(R.id.showTwoButtonDialogBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showNoButtonDialogBtn:
                mNoButtonDialog.show("现在时间：" + sdf.format(new Date()));
                break;
            case R.id.showOneButtonDialogBtn:
                mOneButtonDialog.show("现在时间：" + sdf.format(new Date()));
                break;
            case R.id.showTwoButtonDialogBtn:
                mTwoButtonDialog.show("现在时间：" + sdf.format(new Date()));
                break;
            case CustomDialog.leftButtonId:
                mTwoButtonDialog.dismiss();
                Toast.makeText(MainActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;
            case CustomDialog.rightButtonId:
                mTwoButtonDialog.dismiss();
                Toast.makeText(MainActivity.this, "重新输入", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
