package com.example.administrator.scrollconflitdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class MyFragment extends Fragment {

    private static final String TAG = "MyFragment_";
    private ListView mListView;
    private int position;
    public static MyFragment getInstance(ArrayList<String> list){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list",list);
//        bundle.putInt("position",position);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.position = getArguments().getInt("position");
        Log.i(TAG,TAG+"onAttach:"+position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
        Log.i(TAG,TAG+"onCreate:"+position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,TAG+"onCreateView:"+position);
        mListView = (ListView) inflater.inflate(R.layout.fragment_list,container,false);
        List<String> list = (List<String>) getArguments().getSerializable("list");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(adapter);
        return mListView;
    }

    @Override
    public void onStart() {
        Log.i(TAG,TAG+"onStart:"+position);
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG,TAG+"onResume:"+position);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG,TAG+"onPause:"+position);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG,TAG+"onStop:"+position);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,TAG+"onDestroyView:"+position);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,TAG+"onDestroy:"+position);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i(TAG,TAG+"onDetach:"+position);
        super.onDetach();
    }
}
