package com.example.yaolei.fragmentdemo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Fragment mFragmentOne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                FragmentManager manager = getFragmentManager();
                mFragmentOne =  manager.findFragmentById(R.id.fragment_container);

                if(mFragmentOne != null){
                    manager.popBackStack();
                }else{
                    FragmentTransaction transaction = manager.beginTransaction();
                    mFragmentOne = new FragmentOne();
                    transaction.add(R.id.fragment_container,mFragmentOne);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
        }
    }

}
