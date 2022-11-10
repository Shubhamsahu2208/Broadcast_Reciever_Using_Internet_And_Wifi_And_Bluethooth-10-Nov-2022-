package com.example.broadcastrecieverusinginternetandwifiandbluethooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.core.net.ConnectivityManagerCompat;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            if(isOnline(context)){

                Toast.makeText(context, "Network Connection", Toast.LENGTH_LONG).show();
            }
            else{

                Toast.makeText(context, "No Network Connection", Toast.LENGTH_LONG).show();

            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo!=null && networkInfo.isConnected());
        }catch(NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }
}