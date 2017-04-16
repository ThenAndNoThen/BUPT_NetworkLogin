package com.example.administrator.schoolnetworklogin_out.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.schoolnetworklogin_out.R;
import com.example.administrator.schoolnetworklogin_out.presenter.LoginAndExit;
import com.example.administrator.schoolnetworklogin_out.presenter.impl.LoginAndExitImpl;
import com.example.administrator.schoolnetworklogin_out.util.AccountInfo;
import java.util.concurrent.Callable;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, DialogInterface.OnCancelListener {

    private Button mLoginButton;
    private LoginAndExit mLoginAndExit;
    private EditText mEditTextClassid,mEditTextPassword;
    private TextView mTextViewUsername,mTextViewPassword;
    private TextView mTextViewUsedTraffic,mTextViewBalance;
    private TextView mTExtViewUnitName;
    private ImageView mImageViewErer,mImageViewSansan;
    private LinearLayout mLinearLayoutAccountInfo,mLinearLayoutLoginInfo;
    private CheckBox mCheckBox;
    private CustomProgressBar mProgressBar;
    private boolean isWaiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginButton = (Button) findViewById(R.id.button_login);
        mEditTextClassid = (EditText) findViewById(R.id.editText_classid);
        mEditTextPassword = (EditText) findViewById(R.id.editText_password);
        mCheckBox  = (CheckBox) findViewById(R.id.checkBox);
        mTextViewUsername = (TextView) findViewById(R.id.textView_username);
        mTextViewPassword = (TextView) findViewById(R.id.textView_password);
        mImageViewErer = (ImageView) findViewById(R.id.imageView_erer);
        mImageViewSansan = (ImageView) findViewById(R.id.imageView_sansan);
        mTextViewUsedTraffic = (TextView) findViewById(R.id.textView_usedTraffic);
        mTextViewBalance = (TextView) findViewById(R.id.textView_balance);
        mLinearLayoutAccountInfo = (LinearLayout) findViewById(R.id.layout_accountInfo);
        mLinearLayoutLoginInfo = (LinearLayout) findViewById(R.id.layout_loginInfo);
        mTExtViewUnitName = (TextView)findViewById(R.id.textView_unitName);

        mLoginAndExit = new LoginAndExitImpl();
        isWaiting = false;

        mLoginButton.setOnClickListener(this);
        mEditTextClassid.setOnFocusChangeListener(this);
        mEditTextPassword.setOnFocusChangeListener(this);
    }

    @Override
    synchronized  public void  onClick(View v) {
        switch(v.getId()){
            case R.id.button_login:
                if(isWaiting==true){
                    return;
                }
                if(mLoginButton.getText().toString().equals(getString(R.string.login))){
                    if(mEditTextClassid.getText().toString().equals("") ||
                            mEditTextPassword.getText().toString().equals("")){
                        Toast.makeText(this,"请输入学号和密码！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mEditTextClassid.getText().toString().length()!=10){
                        Toast.makeText(this,"请输入10位长度的学号！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    isWaiting=true;
                    mProgressBar = new CustomProgressBar(this);
                    mProgressBar.setCancelable(true);
                    mProgressBar.setCanceledOnTouchOutside(true);
                    mProgressBar.setOnCancelListener(this);
                    mProgressBar.show();
                    login();
                }else{
                    isWaiting=true;
                    mProgressBar = new CustomProgressBar(this);
                    mProgressBar.setCancelable(true);
                    mProgressBar.setCanceledOnTouchOutside(true);
                    mProgressBar.setOnCancelListener(this);
                    mProgressBar.show();
                    logout();
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.editText_password:
                if(hasFocus){
                    mImageViewErer.setImageResource(R.mipmap.ic_22_hide);
                    mImageViewSansan.setImageResource(R.mipmap.ic_33_hide);
                    mTextViewPassword.setTextColor(getResources().getColorStateList(R.color.colorPrimary));
                }else{
                    mImageViewErer.setImageResource(R.mipmap.ic_22);
                    mImageViewSansan.setImageResource(R.mipmap.ic_33);
                    mTextViewPassword.setTextColor(getResources().getColorStateList(R.color.lightGruy));
                }
                break;
            case R.id.editText_classid:
                if(hasFocus){
                    mTextViewUsername.setTextColor(getResources().getColorStateList(R.color.colorPrimary));
                }
                else{
                    mTextViewUsername.setTextColor(getResources().getColorStateList(R.color.lightGruy));
                }

        }

    }
    private void loginFail(){
        Toast.makeText(this,"用户名或密码错误！",Toast.LENGTH_SHORT).show();
    }

    private void loginSuccess(){
        getAccountInfo();
    }

    private void login(){
        Observable<String> loginObservable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() {
                return mLoginAndExit.login(mEditTextClassid.getText().toString(),mEditTextPassword.getText().toString(),mCheckBox.isChecked());
            }
        });
        loginObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onNext(String respon){
                        if(respon==null){
                            Toast.makeText(MainActivity.this,MainActivity.this.getResources().getString(R.string.connect_fail_info),Toast.LENGTH_SHORT).show();
                            mProgressBar.dismiss();
                            isWaiting = false;
                            return;
                        }
                        if(respon.equals(LoginAndExit.CALL_CANCELED)){
                            isWaiting = false;
                            return;
                        }
                        if(respon.equals(LoginAndExit.TOO_FAST)){
                            Toast.makeText(MainActivity.this,respon,Toast.LENGTH_SHORT).show();
                            mProgressBar.dismiss();
                            isWaiting = false;
                            return;
                        }
//                        mProgressBar.dismiss();
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                isWaiting = false;
                            }
                        }).start();

                        Log.i("respon.body：",respon);
                        if(respon.contains("登录成功窗")){
                            Toast.makeText(MainActivity.this,MainActivity.this.getResources().getString(R.string.connect_success),Toast.LENGTH_SHORT).show();
                            loginSuccess();
                        }
                        else{
                            mProgressBar.dismiss();
                            loginFail();
                        }
                    }
                });
    }

    private void logout(){
        Observable<String> loginObservable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() {
                return mLoginAndExit.logout();
            }
        });
        loginObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onNext(String respon){
                        if(respon==null){
                            Toast.makeText(MainActivity.this,MainActivity.this.getResources().getString(R.string.connect_fail_info),Toast.LENGTH_SHORT).show();
                            mProgressBar.dismiss();
                            isWaiting = false;
                            return;
                        }
                        if(respon.equals(LoginAndExit.CALL_CANCELED)){
                            isWaiting = false;
                            return;
                        }
                        if(respon.equals(LoginAndExit.TOO_FAST)){
                            Toast.makeText(MainActivity.this,respon,Toast.LENGTH_SHORT).show();
                            mProgressBar.dismiss();
                            isWaiting = false;
                            return;
                        }
                        mProgressBar.dismiss();
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                isWaiting = false;
                            }
                        }).start();

                        Log.i("respon.body：",respon);
                        if(respon.contains("Msg=14")){
                            logoutSuccess();
                        }
                        else{
                            logoutFail();
                        }

                    }
                });
    }

    private void logoutFail(){
        Toast.makeText(this,"注销失败,请重试！",Toast.LENGTH_SHORT).show();
    }
    private void logoutSuccess(){
        mLinearLayoutLoginInfo.setVisibility(View.VISIBLE);
        mLinearLayoutAccountInfo.setVisibility(View.INVISIBLE);
        mLoginButton.setText(R.string.login);
    }

    private void initData(){
//        getAccountInfo();
        SharedPreferences sp = getSharedPreferences("account", Context.MODE_PRIVATE);
        boolean isRemberPassword = sp.getBoolean("isRemberPassword",false);
        String username = sp.getString("username", "");
        mEditTextClassid.setText(username);
        if(isRemberPassword) {
            String password = sp.getString("password", "");
            mEditTextPassword.setText(password);
            mCheckBox.setChecked(true);
        }
    }

    private void getAccountInfo(){
        Observable<AccountInfo> loginObservable = Observable.fromCallable(new Callable<AccountInfo>() {
            @Override
            public AccountInfo call() {
                return mLoginAndExit.isLogined();
            }
        });
        loginObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccountInfo>() {

                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onNext(AccountInfo info){
                        if(info==null){
                            Toast.makeText(MainActivity.this,MainActivity.this.getResources().getString(R.string.connect_fail_info),Toast.LENGTH_SHORT).show();
                            mProgressBar.dismiss();
                            return;
                        }
                        if(info.equals(LoginAndExit.CALL_CANCELED)){
                            return;
                        }
                        mProgressBar.dismiss();
                        if(info.getBalance()!=-1){
                            Log.i("已登录", "onNext: "+info.getUsedMB()+","+info.getBalance());
                            mLinearLayoutLoginInfo.setVisibility(View.INVISIBLE);
                            if(info.getUsedMB()<1024){
                                mTextViewUsedTraffic.setText(String.valueOf(info.getUsedMB()));
                                mTExtViewUnitName.setText(" MB");
                            }else{
                                mTextViewUsedTraffic.setText(String.valueOf(info.getUsedGB()));
                                mTExtViewUnitName.setText(" GB");
                            }
                            mTextViewBalance.setText(String.valueOf(info.getBalance()));
                            mLinearLayoutAccountInfo.setVisibility(View.VISIBLE);
                            mLoginButton.setText(R.string.logout);
                        }
                        else{
                            mLinearLayoutLoginInfo.setVisibility(View.VISIBLE);
                            mLinearLayoutAccountInfo.setVisibility(View.INVISIBLE);
                            mLoginButton.setText(R.string.login);
                            Log.i("未登录", "onNext: ");
                        }

                    }
                });
    }
    @Override
    protected void onResume(){
        super.onResume();
//        mProgressBar = new CustomProgressBar(this);
//        mProgressBar.setCancelable(true);
//        mProgressBar.setCanceledOnTouchOutside(true);
//        mProgressBar.setOnCancelListener(this);
//        mProgressBar.show();
        initData();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mLoginAndExit.cancelCall();
        Log.i("onCancel","取消请求");
    }
}
