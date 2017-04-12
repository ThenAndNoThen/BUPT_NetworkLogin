package com.example.administrator.schoolnetworklogin_out.util;

import android.app.Application;

/**
 * Created by Administrator on 2016/12/16 0016.
 */

public class AccountInfo {
    int mUsedMB;
    double mBalance;
    double mUsedGB;
    public AccountInfo(){
        this.mBalance=-1;
    }
    public AccountInfo(int mUsedMB,double mBalance,double mUsedGB){
        this.mBalance=mBalance;
        this.mUsedMB=mUsedMB;
        this.mUsedGB=mUsedGB;
    }

    public double getBalance() {
        return mBalance;
    }

    public int getUsedMB() {
        return mUsedMB;
    }

    public double getUsedGB() {
        return mUsedGB;
    }

    public void setBalance(double balance) {
        mBalance = balance;
    }

    public void setUsedMB(int usedMB) {
        mUsedMB = usedMB;
    }

    public void setUsedGB(double usedGB) {
        mUsedGB = usedGB;
    }
}
