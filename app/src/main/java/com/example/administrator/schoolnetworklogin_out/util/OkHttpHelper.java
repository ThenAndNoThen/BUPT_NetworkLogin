package com.example.administrator.schoolnetworklogin_out.util;

import android.util.Log;

import com.example.administrator.schoolnetworklogin_out.presenter.LoginAndExit;

import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class OkHttpHelper {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client;
    Call call;
    public OkHttpHelper(){
        client=new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS)
                .build();
    }
    public synchronized String post(String url, String cookie, String bodystr){
//        String json="{";
//        Iterator<Map.Entry<String , String>> entries = data.entrySet().iterator();
//        while(entries.hasNext()){
//            Map.Entry<String, String> entry = entries.next();
//            json+="\""+entry.getKey()+"\":\""+entry.getValue()+"\",";
//        }
//        json+="\"0MKKey\": \""+"\"";
//        if(json.endsWith(",")){
//            json=json.substring(0,json.length()-1);
//        }
//        json+="}";
//        json="savePWD=0&upass=111180250&DDDDD=2016110718&0MKKey=";
        RequestBody body = RequestBody.create(JSON, bodystr);
        Request request = null;

        if(cookie!=null){
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Cookie",cookie)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
        Response response = null;
        String responbody=null;
        try {
            call=client.newCall(request);
            response = call.execute();
            byte s[]=response.body().bytes();
            responbody=new String(s,"gb2312");

//            responbody=response.body().string();
//            response.close();
//          Log.i("respon.body：",responbody);
        }catch(SocketException e){
            responbody = LoginAndExit.CALL_CANCELED;
        }
        catch(IOException e){
            e.printStackTrace();
            responbody=LoginAndExit.TOO_FAST;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responbody;
    }

    public synchronized String get(String url, String cookie){
        Request request = null;
        if(cookie!=null){
            request = new Request.Builder()
                    .url(url)
                    .header("Cookie",cookie)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(url)
                    .build();
        }
        Response response = null;
        String responbody=null;
        try {
            call = client.newCall(request);
            response = call.execute();
            byte s[]=response.body().bytes();
            responbody=new String(s,"gb2312");
//            responbody=response.body().string();
//            response.close();
//          Log.i("respon.body：",responbody);
        }catch(SocketException e){
            responbody = LoginAndExit.CALL_CANCELED;
        }catch(IOException e){
            e.printStackTrace();
            responbody=LoginAndExit.TOO_FAST;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responbody;
    }
    public void cancelCall(){
        call.cancel();
    }
}
