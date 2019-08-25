package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ENABLE_BT = 0;
    public static final int REQUEST_DISCOVER_BT = 1;
    public ImageView mBlueTv;

    Dialog myDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
    }

    public void ShowPopup(View v) {

        final TextView txtclose;
        final TextView mStatusBlueTv, mPairedTv;
        final ImageView mBlueTv;
        final Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;
        final BluetoothAdapter mBlueAdapter;


        mStatusBlueTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairedTv);
        mBlueTv = findViewById(R.id.bluetoothTv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBlueAdapter == null) {
            mStatusBlueTv.setText("BlueTooth is not available");
        } else {
            mStatusBlueTv.setText("BlueTooth is available");
        }


        // set image according to bluetooth status on/off
        if (mBlueAdapter.isEnabled()) {
            mBlueTv.setImageResource(R.drawable.ic_action_on);
        } else {
            mBlueTv.setImageResource(R.drawable.ic_action_off);
        }

        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBlueAdapter.isEnabled()) {
                    showToast("Turning on BlueTooth...");

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    showToast("BlueTooth is already on");
                }

            }
        });

        // make tablet discoverable
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBlueAdapter.isDiscovering()) {
                    showToast("Making your device discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }


            }
        });

        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBlueAdapter.isEnabled()) {
                    mBlueAdapter.disable();
                    showToast("Turning Bluetooth Off");
                    mBlueTv.setImageResource(R.drawable.ic_action_off);
                } else {
                    showToast("Bluetooth is already Off");
                }

            }
        });


        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBlueAdapter.isEnabled()) {
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device : devices) {
                        mPairedTv.append("\nDevice: " + device.getName() + "," + device);
                    }
                } else {
                    //bluetooth is off so cannot connect to paired device
                    showToast("Turn on bluetooth to get paired devices");
                }

            }
        });


        myDialog.setContentView(R.layout.popup);
        txtclose = (TextView) myDialog.findViewById(R.id.closepop);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        // not sure what this is for, might have to remove later
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //bluetooth is on
                    mBlueTv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is on");
                } else {
                    //user denied to turn bluetooth on
                    showToast("couldn't on BlueTooth");
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //toast message function
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
