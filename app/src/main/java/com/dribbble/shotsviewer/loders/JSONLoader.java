package com.dribbble.shotsviewer.loders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dribbble.shotsviewer.adapters.RecyclerViewAdapter;
import com.dribbble.shotsviewer.data.Shot;
import com.dribbble.shotsviewer.db.DataBase;
import com.dribbble.shotsviewer.logic.JSONParser;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import info.guardianproject.netcipher.NetCipher;


public class JSONLoader extends AsyncTask<Void,Integer,String>{
    final String HTTPS_URL = "https://api.dribbble.com/v1/shots?access_token=2a41fea1d3b9f39dd8637388ab84c69f333153fac82959404eb3a1b72a5d6af2&date=";
    private DataBase dataBase;
    private String date ;
    private RecyclerViewAdapter adapter;

    public JSONLoader(Context context, String date , RecyclerViewAdapter adapter) {
        this.dataBase = new DataBase(context);
        this.date = date;
        this.adapter = adapter;
    }




    @Override
    protected String doInBackground(Void... params) {
        try {
            Log.e("url",HTTPS_URL+date);
            URL urlJSON = new URL(HTTPS_URL+date);
            HttpsURLConnection connection = NetCipher.getHttpsURLConnection(urlJSON);
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("JSON",e.toString());
            return "Error";
        } catch (IOException e) {
            Log.e("JSON",e.toString());
            e.printStackTrace();
            return "Error";
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("JSON",s);
        JSONParser jsonParser = new JSONParser(s);
        try {

            for(Shot shot :jsonParser.parseToShorts()){
                dataBase.insertData(shot);
            }
            adapter.update(dataBase.getAllDataFromDB());
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
