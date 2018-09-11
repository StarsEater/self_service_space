package com.example.qfz.selfservicespace.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.model.QRCodeUtil;
import com.example.qfz.selfservicespace.model.device_adapter;
import com.example.qfz.selfservicespace.object.User;
import com.example.qfz.selfservicespace.object.device;
import com.example.qfz.selfservicespace.object.deviceList;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;

public class reservation2device extends AppCompatActivity {
    private RecyclerView recyclerView;
    private device_adapter  adapter;
    private deviceList devices = new deviceList();
    private User user;
    private Button twodimcodebtn;
    private PopupWindow mPop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation2device);
        initdevice();
        recyclerView = (RecyclerView) findViewById(R.id.reservation2device_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new device_adapter(devices, user);
        recyclerView.setAdapter(adapter);

        twodimcodebtn=findViewById(R.id.twodimecodebtn);
        twodimcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = getLayoutInflater().inflate(R.layout.twodimecode, null);
                ImageView im = view1.findViewById(R.id.twodimecode);
                Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(devices.qrcode, 800, 800);
                im.setImageBitmap(mBitmap);
                mPop = new PopupWindow(view1, 800, 800);
                mPop.setOutsideTouchable(true);
                mPop.setFocusable(true);
                mPop.showAsDropDown(twodimcodebtn);
            }
        });
    }

    private void initdevice() {
        Intent intent = getIntent();
        deviceList deviceList1 = (deviceList) intent.getSerializableExtra("deviceList");
        devices = deviceList1;
        user = (User) intent.getSerializableExtra("UserInfo");
    }
}
