package com.example.qfz.selfservicespace.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.ReservationInfo;
import com.example.qfz.selfservicespace.object.StoreList;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReservationFinishedActivity extends AppCompatActivity {

    @InjectView(R.id.form_id)
    EditText reservationID;
    @InjectView(R.id.form_shopname)
    EditText shopName;
    @InjectView(R.id.form_times)
    EditText time;
    @InjectView(R.id.form_content)
    EditText tables;
    @InjectView(R.id.form_cheap)
    EditText discount;
    @InjectView(R.id.form_gua)
    EditText bail;
    @InjectView(R.id.form_total)
    EditText total;
    @InjectView(R.id.form_phone)
    EditText phoneNumber;

    @InjectView(R.id.form_sure)
    Button confirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_reservation);
        ButterKnife.inject(this);

        final Intent intent = getIntent();
        ReservationInfo reservation = (ReservationInfo) intent.getSerializableExtra("Reservation");

        reservationID.setText(reservation.reservationID);
        shopName.setText(reservation.storeName);
        time.setText(reservation.time);
        tables.setText(reservation.tables.toString());
        discount.setText(reservation.discount + "");
        bail.setText(reservation.bail + "");
        total.setText(reservation.total + "");
        phoneNumber.setText(reservation.phoneNumber);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSir("13120325277");
            }
        });
    }

    private void callSir(String phoneNum) {
        Log.d("call", "callSir: " + phoneNum);
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }
}
