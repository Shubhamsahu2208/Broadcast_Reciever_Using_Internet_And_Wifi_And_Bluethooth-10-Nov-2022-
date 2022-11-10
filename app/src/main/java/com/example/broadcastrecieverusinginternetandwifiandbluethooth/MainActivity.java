package com.example.broadcastrecieverusinginternetandwifiandbluethooth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT =1;

    BroadcastReceiver broadcastReceiver;

    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;
    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;

    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBlueTv = findViewById(R.id.tv_statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairedTv);

        mBlueIv = findViewById(R.id.iv_bluetooth);
        mOnBtn = findViewById(R.id.btn_turnOn);
        mOffBtn = findViewById(R.id.btn_turnOff);

        mDiscoverBtn = findViewById(R.id.btn_discoverable);
        mPairedBtn = findViewById(R.id.btn_getpaired);

    broadcastReceiver = new NetworkChangeReceiver();
    registerNetworkBroadcastReciever();

    bluetooth();


    }

    protected void bluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter == null){
            mStatusBlueTv.setText("Bluetooth is not Available");
        }
        else{
            mStatusBlueTv.setText("Bluetooth is Available");
        }

        if(bluetoothAdapter.isEnabled()){
            mBlueIv.setImageResource(R.drawable.ic_action_name2);
        }
        else{
            mBlueIv.setImageResource(R.drawable.ic_action_name1);
        }

        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                if(!bluetoothAdapter.isEnabled()){
                    showToast("Turing on Bluetooth...");

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult( intent ,REQUEST_ENABLE_BT);
                }
                else{
                    showToast("Bluetooth is already  on");
                }

            }
        });

        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if(!bluetoothAdapter.isDiscovering()){

                    showToast("Making Your Device Discovering");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult( intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.disable();
                    showToast("Turning Bluetooth OFF");
                    mBlueIv.setImageResource(R.drawable.ic_action_name1);

                }
                else {
                    showToast("Bluetooth is already Off");
                }

            }
        });

        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                if(bluetoothAdapter.isEnabled()){
                    mPairedTv.setText("Pair Devices");
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    for(BluetoothDevice device : devices){
                        mPairedTv.append("\nDevice"+ device.getName() +","+device);
                    }
                }

                else{
                    showToast("Turn on Bluetooth to get paired Devices");
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){

                    mBlueIv.setImageResource(R.drawable.ic_action_name1);
                    showToast("Bluetooth is ON");
                }
                else{

                    showToast("could't on Bluetooth");
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }


    protected void registerNetworkBroadcastReciever(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }

    protected void unregisterNetwork(){
        try{
            unregisterReceiver(broadcastReceiver);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }


}