package com.example.qfz.selfservicespace.activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.model.reservation_adapter;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.ReservationList;
import com.example.qfz.selfservicespace.object.StoreList;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class fragment_order extends Fragment {
    private RecyclerView recyclerView;
    private ReservationList reservationList = new ReservationList();
    private reservation_adapter adapter;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ReservationImageView;
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
    }

    private void SetView(ReservationList reservationList) {
        initShop(reservationList);
        main_page act = (main_page) getActivity();
        ArrayList<Reservation> temp = act.reservationList.reservations;;
        if (getActivity() == null)
            Log.d("onCreateView", "getActivity==null");
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new reservation_adapter(temp, act.user);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_order,container,false);
        recyclerView=view.findViewById(R.id.order_recycler);
        toolbar = view.findViewById(R.id.toolbar);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        ReservationImageView = view. findViewById(R.id.reservation_image_view);
        main_page act = (main_page) this.getActivity();
        SetView(act.reservationList);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //对TOOLBAT的设置
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar)view.findViewById(R.id.toolbar_search));
        setHasOptionsMenu(true);
        collapsingToolbar.setTitle("你的全部订单");
        Glide.with(getActivity()).load("https://cdn.dribbble.com/users/41854/screenshots/3761050/pride_twitter.gif").into(ReservationImageView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置初始时间，结束时间，人数监听事件

    }

    private void initShop(ReservationList ReservationList) {
        reservationList = ReservationList;
    }

}
