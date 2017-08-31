package com.example.yaolei.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isCanWrite = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if(isCanWrite){
            testWriteFile();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }
    }


    private void testInternet(){
        new Thread(){
            @Override
            public void run() {
                //连网
                try {
                    Socket socket = new Socket("www.baidu.com",80);
                    Log.d("info_","info_"+socket.isConnected());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void testWriteFile(){
        try {
            FileOutputStream fos = new FileOutputStream("/mnt/sdcard/hahaha.txt");
            fos.write("kdjflajdfjasdkf".getBytes());
            fos.close();
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                testWriteFile();
            }else{
                showJumpDialog();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showJumpDialog() {
        new AlertDialog.Builder(this).setMessage("需要开启内存权限").setNeutralButton("去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                Intent localIntent = new Intent();
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivityForResult(localIntent,200);
            }
        }).setCancelable(false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            Log.d("info_","info_"+resultCode);
            boolean isCanWrite = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if(isCanWrite){
                testWriteFile();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }
}
