package com.dribbble.shotsviewer.logic;

import android.content.Context;
import android.util.Log;

import com.dribbble.shotsviewer.data.Shot;

import org.json.JSONArray;
import org.json.JSONException;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by kirill on 18.06.17.
 */

public class JSONParser {
    private final long MILLISECONDS_PER_DAY = 86400000;
    private String json;


    public JSONParser(String json) {
        this.json = json;
    }

    public ArrayList<Shot> parseToShorts() throws JSONException {
        ArrayList <Shot> shots = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        for(int i = 0 ; i < jsonArray.length(); i++){

            if(!jsonArray.getJSONObject(i).getBoolean("animated")){
                Shot shot = new Shot();
                shot.setId(jsonArray.getJSONObject(i).getInt("id"));
                shot.setTitle(jsonArray.getJSONObject(i).getString("title"));
                shot.setDescription(fixDescriptions(jsonArray.getJSONObject(i).getString("description")));
                if (shot.getDescription().equals("null")){
                    shot.setDescription("");
                }
                shot.setImageURL(jsonArray.getJSONObject(i).getJSONObject("images").getString("hidpi"));
                if(shot.getImageURL().equals("null")){
                    shot.setImageURL(jsonArray.getJSONObject(i).getJSONObject("images").getString("normal"));
                }

                String updated_at = jsonArray.getJSONObject(i).getString("updated_at").substring(0,10);
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = dataFormat.parse(updated_at);
                    shot.setUnixDate(date.getTime());
                    shot.setDay(date.getTime()/MILLISECONDS_PER_DAY);
                } catch (ParseException e) {
                    e.printStackTrace();
                    shot.setUnixDate(new Date().getTime());
                    shot.setDay(new Date().getTime()/MILLISECONDS_PER_DAY);
                }

                shots.add(shot);
                Log.e("shot",shot.toString());
            }
        }
        return shots;

    }

    private String fixDescriptions(String jsonTitle){
        return jsonTitle.replaceAll("(?:<).*?(?:>)|\\n"," ").replaceAll("\\s{2,}"," ");
    }
}
