package com.example.administrator.schoolnetworklogin_out.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.administrator.schoolnetworklogin_out.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Administrator on 2016/12/17 0017.
 */

public class CustomProgressBar extends Dialog {
    private Context context ;
    private AVLoadingIndicatorView mAVLoadingIndicatorView;
    public CustomProgressBar(Context context) {
        super(context, R.style.dialog_theme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progressbar) ;
    }

    @Override
    public void show() {
        try{
            if(!isShowing()){
                super.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mAVLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avloadingView);
    }
}
