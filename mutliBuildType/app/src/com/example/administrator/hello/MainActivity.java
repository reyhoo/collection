package com.example.administrator.hello;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, TAG + "::onCreate()::");
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentEnvironment = appInfo.metaData
                .getString("CurrentEnvironment");
        String DeviceType = appInfo.metaData
                .getString("DeviceType");
        tv = (TextView) findViewById(R.id.tv);
        tv.setText("Evn:" + currentEnvironment + ";DeviceType"+DeviceType+";app_name:" + getString(R.string.app_name));

//        if (savedInstanceState != null) {
//            tv.append(savedInstanceState.getString("temp", ""));
//
//        }
        int margin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
//        tv.setText(margin + "");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, TAG + "::onSaveInstanceState()::");
        super.onSaveInstanceState(outState);
        outState.putString("temp", "" + System.currentTimeMillis());

    }

    public void doClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, TAG + "::onDestroy()::");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Cursor c = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            c.moveToNext();
            String path = c.getString(0);
            Log.i("Hello","Hello::onActivityResult()::"+path);
        }

    }
}
