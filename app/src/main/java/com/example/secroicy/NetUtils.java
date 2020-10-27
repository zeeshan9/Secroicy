package com.example.secroicy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


public class NetUtils {

    public static String baseURL = "http://137.226.182.185:";
    public static String baseURServer = baseURL+"5000/";
    public static String baseURLClient = baseURL+"3000/";

    public static boolean isNetConnected(Context context) {
        try {
            ConnectivityManager connManger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connManger!=null)
            {
                NetworkInfo netInfo=connManger.getActiveNetworkInfo();
                if(netInfo!=null&&netInfo.isConnected()){
                    if(netInfo.getState()== NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(context,"Connectivity Manager is not detected", Toast.LENGTH_LONG).show();

        }
        return false;
    }


}