package com.example.administrator.schoolnetworklogin_out.presenter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.administrator.schoolnetworklogin_out.presenter.LoginAndExit;
import com.example.administrator.schoolnetworklogin_out.util.AccountInfo;
import com.example.administrator.schoolnetworklogin_out.util.MyApplication;
import com.example.administrator.schoolnetworklogin_out.util.OkHttpHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class LoginAndExitImpl implements LoginAndExit {

    private OkHttpHelper mOkhttp;
    public LoginAndExitImpl(){
    }

    @Override
    public String login(String username, String password, boolean remberPassword) {
        String url= MyApplication.HOST;
//        String cookie="myusername="+username+"; pwd="+password+"; username="+username+"; smartdot="+password;
        String body = "savePWD=0&upass="+password+"&DDDDD="+username+"&0MKKey=";
        mOkhttp=new OkHttpHelper();
        try {
            SharedPreferences sp = MyApplication.getInstance().getSharedPreferences("account", Context.MODE_PRIVATE);
            if (remberPassword) {
                sp.edit().putString("username", username).putString("password", password).putBoolean("isRemberPassword", true).commit();
            } else {
                sp.edit().putString("username", username).putBoolean("isRemberPassword", false).commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mOkhttp.post(url, null, body);
    }

    @Override
    public String logout() {
        String url= MyApplication.HOST+"/F.htm";
        mOkhttp=new OkHttpHelper();
        return mOkhttp.get(url,null);
    }

    @Override
    public AccountInfo isLogined() {
        AccountInfo accountInfo = null;
        try {
            String url = MyApplication.HOST;
            mOkhttp = new OkHttpHelper();
            String respon = mOkhttp.get(url, null);
            if (respon.contains("上网注销窗")) {
                Log.i(TAG, "isLogined: " + respon);
                String userMB = respon.substring(respon.indexOf("flow") + 6);
                userMB = userMB.substring(0, userMB.indexOf(" "));
                Log.i(TAG, "isLogined: " + userMB);
                String banalce = respon.substring(respon.indexOf("fee") + 5);
                banalce = banalce.substring(0, banalce.indexOf(" "));
                Log.i(TAG, "isLogined: " + banalce);
                int intUsedMB = Integer.valueOf(userMB);
                double doubleUsedGB = Integer.valueOf(userMB);
                double doubleBalance = Double.valueOf(banalce);
                DecimalFormat df   = new DecimalFormat("######0.00");
                doubleUsedGB=Double.valueOf(df.format(doubleUsedGB/(1024*1024)));
                accountInfo = new AccountInfo(intUsedMB / (1024), doubleBalance / 10000,doubleUsedGB);
            }else{
                accountInfo=new AccountInfo();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountInfo;
    }
    public void cancelCall(){
        mOkhttp.cancelCall();
    }
}
