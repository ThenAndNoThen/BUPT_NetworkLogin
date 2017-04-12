package com.example.administrator.schoolnetworklogin_out.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class MyApplication extends Application {
    public final static  String HOST="http://10.3.8.211";
    private static MyApplication myApplication;
    @Override
    public void onCreate(){
        super.onCreate();
        myApplication=this;
    }

    public static MyApplication getInstance(){
        return myApplication;
    }
}
