package com.example.administrator.schoolnetworklogin_out.util;

/**
 * Created by Then on 2017/5/6.
 */

public class LocalAccountBean {
    boolean isRemembered;
    String username;
    String password;

    public LocalAccountBean(boolean isRemembered, String username, String password){
        this.isRemembered=isRemembered;
        this.username=username;
        this.password=password;
    }
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isRemembered() {
        return isRemembered;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRemembered(boolean remembered) {
        isRemembered = remembered;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
