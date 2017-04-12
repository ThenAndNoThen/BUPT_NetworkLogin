package com.example.administrator.schoolnetworklogin_out.presenter;

import com.example.administrator.schoolnetworklogin_out.util.AccountInfo;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public interface LoginAndExit {
    String CALL_CANCELED="请求取消";//当请求被用户结束的时候的okhttp返回值
    String TOO_FAST="出错啦，稍后再试";
    String login(String username, String password , boolean remberPassword);
    String logout();
    AccountInfo isLogined();
    void cancelCall();
}
