package com.dribbble.shotsviewer.ui;



import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.dribbble.shotsviewer.db.DataBase;
import com.dribbble.shotsviewer.loders.JSONLoader;
import com.dribbble.shotsviewer.R;
import com.dribbble.shotsviewer.adapters.RecyclerViewAdapter;
import com.dribbble.shotsviewer.data.Shot;
import com.dribbble.shotsviewer.logic.DataCleaner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Shot> shots;
    private DataBase dataBase;
    private ProgressBar progressBar;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private long maxUnixDate;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        dataBase = new DataBase(getApplicationContext());

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        MainAsyncTask asyncTask = new MainAsyncTask();
        asyncTask.execute();

    }


    private class MainAsyncTask extends  AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            maxUnixDate = dataBase.getMaxUnixDateFromDB();
            if(maxUnixDate == 0){
                Date date = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
                JSONLoader jsonLoader = new JSONLoader(getApplicationContext(),formatForDateNow.format(date) , new RecyclerViewAdapter(new ArrayList<Shot>()));
                jsonLoader.execute();
            }
            else{
                progressBar.setVisibility(View.INVISIBLE);
                long maxDay= dataBase.getMaxDayFromDB();
                Log.e("checkMaxDay", Long.toString(maxDay));
            }

        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            shots = dataBase.getAllDataFromDB();
            recyclerView = (RecyclerView) findViewById(R.id.rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            adapter = new RecyclerViewAdapter(shots);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemViewCacheSize(10);

            final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(dataBase.countData() > 50){
                        DataCleaner dataCleaner = new DataCleaner(getApplicationContext());
                        dataCleaner.cleanCache();
                    }
                    Date date = new Date();
                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
                    JSONLoader jsonLoader = new JSONLoader(getApplicationContext(),formatForDateNow.format(date), adapter);
                    jsonLoader.execute();
                    swipeRefreshLayout.setRefreshing(false);

                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }



}