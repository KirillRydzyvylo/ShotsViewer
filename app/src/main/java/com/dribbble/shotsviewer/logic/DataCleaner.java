package com.dribbble.shotsviewer.logic;

import android.content.Context;

import com.dribbble.shotsviewer.data.Shot;
import com.dribbble.shotsviewer.db.DataBase;
import com.dribbble.shotsviewer.loders.ImageCache;

import java.util.ArrayList;


public class DataCleaner {
    private Context context;
    private DataBase dataBase;

    public DataCleaner(Context context) {
        this.context = context;
        dataBase = new DataBase(context);
    }


    public void cleanCache(){
       while(dataBase.countData() > 50) {
            long minUnixDay = dataBase.getMinUnixDateFromDB();
            ArrayList<Shot> shots = dataBase.getShotsForMinUnixDay(minUnixDay);
            ImageCache imageCache = new ImageCache(context);
            for(Shot shot : shots){
                imageCache.deleteFile(shot.getImageURL());
                dataBase.deleteOldData(shot.getId());
            }
        }
    }


}
