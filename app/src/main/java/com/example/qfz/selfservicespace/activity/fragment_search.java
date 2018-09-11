package com.example.qfz.selfservicespace.activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.model.Time_data_selector;
import com.example.qfz.selfservicespace.model.number_of_people_show;
import com.example.qfz.selfservicespace.model.shop_adapter;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.ConnectionRes;
import com.example.qfz.selfservicespace.object.SearchStoreInfo;
import com.example.qfz.selfservicespace.object.StoreList;
import com.example.qfz.selfservicespace.object.shop;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class fragment_search extends Fragment {
    private TextView begin_time,end_time,people_num;
    private SearchView mSearchView;
    private RecyclerView recyclerView;
    private TextView title;
    private StoreList shopList = new StoreList();
    private shop_adapter adapter;
    private SearchStoreInfo searchStoreInfo = new SearchStoreInfo();

    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        //Toast.makeText(getActivity(),"hiiiii",Toast.LENGTH_SHORT).show();
        final MenuItem searchItem = menu.findItem(R.id.menu_search);

        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_time", null, null);

//search 监听事件
//搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                title.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Open", Toast.LENGTH_SHORT).show();
            }
        });
//搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                Log.e("CSDN_LQR", "TextSubmit : " + s);
                searchStoreInfo.searchKey = s;
                SendRequest(searchStoreInfo.getJson());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("CSDN_LQR", "TextChange --> " + s);
                searchStoreInfo.searchKey = s;
                SendRequest(searchStoreInfo.getJson());
                return false;
            }
        });
    }

    private void SendRequest(final String requestMsg) {
        final Handler myhandler = new Handler() {
            public void handleMessage(Message msg) {
                //isNetError = msg.getData().getBoolean("isNetError");
                if(msg.what==0x123)
                {
                    //更新界面
                    Log.d("Search Stores", msg.getData().getString("msg"));
                    String message = msg.getData().getString("msg");

                    Gson gson = new Gson();
                    ConnectionRes connectionRes = gson.fromJson(message, ConnectionRes.class);

                    if(connectionRes.state == 0) {
                        StoreList storeList = gson.fromJson(message, StoreList.class);

                        if(storeList != null) {
                            SetView(storeList);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        };

        new Thread(){
            public void run(){
                try {
                    //请求服务器返回用户数据
                    Connection connection = new Connection();
                    String msg = connection.connectServer(requestMsg, "/searchStores.json");

                    //如果成功返回
                    //将子线程中获得的用户信息传递到主线程 myhandler 中
                    Bundle bundle = new Bundle();
                    Message message = new Message();

                    bundle.putString("msg", msg);

                    message.setData(bundle);
                    message.what=0x123;
                    myhandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void SetView(StoreList storeList) {
        initShop(storeList);
        if (getActivity() == null)
            Log.d("onCreateView", "getActivity==null");
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);

        main_page act = (main_page) this.getActivity();
        adapter = new shop_adapter(shopList.stores, act.user, searchStoreInfo);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search,container,false);
        begin_time= view.findViewById(R.id.begin_time);
        end_time= view.findViewById(R.id.end_time);
        people_num=view.findViewById(R.id.people_num);
        recyclerView=view.findViewById(R.id.search_recycler);
        title=view.findViewById(R.id.title_search);

        main_page act = (main_page) this.getActivity();
        SetView(act.storeList);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //对TOOLBAT的设置
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar)view.findViewById(R.id.toolbar_search));
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置初始时间，结束时间，人数监听事件
        String temp_date=new String();
        begin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    final Time_data_selector time_data_selector=new Time_data_selector(getActivity());
                    time_data_selector.setsure(
                            new Time_data_selector.sure_listener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void sure(Time_data_selector dialog) {
                                    Toast.makeText(getActivity(),"sure",Toast.LENGTH_SHORT).show();
                                    String s = time_data_selector.getDate()+" "+time_data_selector.getTime();
                                    begin_time.setText(s);
                                    begin_time.setTextSize(20);
                                    searchStoreInfo.beginTime = s;
                                    SendRequest(searchStoreInfo.getJson());
                                }
                            }
                    ).setcancel(new Time_data_selector.cancel_listener() {
                        @Override
                        public void cancel(Time_data_selector dialog) {
                            Toast.makeText(getActivity(),"cancel",Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                    //temp_date=time_data_selector.getDate();

                }
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null){
                    final Time_data_selector time_data_selector1=new Time_data_selector(getActivity());
                    time_data_selector1.setsure(
                            new Time_data_selector.sure_listener() {
                                @RequiresApi(api = Build.VERSION_CODES.M)
                                @Override
                                public void sure(Time_data_selector dialog) {
                                    Toast.makeText(getActivity(),"sure",Toast.LENGTH_SHORT).show();
                                    String s = time_data_selector1.getDate()+" "+time_data_selector1.getTime();
                                    end_time.setText(s);
                                    end_time.setTextSize(20);
                                    searchStoreInfo.endTime = s;
                                    SendRequest(searchStoreInfo.getJson());

                                }
                            }
                    ).setcancel(new Time_data_selector.cancel_listener() {
                        @Override
                        public void cancel(Time_data_selector dialog) {
                            Toast.makeText(getActivity(),"cancel",Toast.LENGTH_SHORT).show();
                        }
                    }).show();

                }
            }
        });
        people_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity()!=null) {
                    final number_of_people_show people_num_show = new number_of_people_show(getActivity());
                    people_num_show.setsure(new number_of_people_show.sure_listener() {
                        @Override
                        public void sure(number_of_people_show dialog) {
                            Toast.makeText(getActivity(),"sure",Toast.LENGTH_SHORT).show();
                            people_num.setText(people_num_show.getnum()+"人");
                            people_num.setTextSize(20);
                            searchStoreInfo.peopleNum = Integer.parseInt(people_num_show.getnum());
                            SendRequest(searchStoreInfo.getJson());
                        }
                    }).setcancel(new number_of_people_show.cancel_listener() {
                        @Override
                        public void cancel(number_of_people_show dialog) {
                            Toast.makeText(getActivity(),"cancel",Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                }
            }
        });
    }

    private void initShop(StoreList storeList) {
        shopList = storeList;
        main_page act = (main_page) this.getActivity();
        act.storeList = storeList;
    }
}
