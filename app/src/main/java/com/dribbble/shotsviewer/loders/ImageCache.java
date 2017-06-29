package com.dribbble.shotsviewer.loders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageCache {
    private File cache;
    private Context context;

    public ImageCache(Context context) {
        this.context = context;
        this.cache = context.getCacheDir();
    }

    public void saveImage(String url, Bitmap bitmap){
        String [] splitUrl = url.split("/");
        String fileName = "/".concat(splitUrl[splitUrl.length-2]).concat(splitUrl[splitUrl.length-1]);
        File file = new File(cache,fileName);

        try {
            FileOutputStream saveImage  = new FileOutputStream(file);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            saveImage.write(bytes.toByteArray());
            saveImage.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap readImage(String url) {
        Bitmap bitmap;
        String [] splitUrl = url.split("/");
        String fileName = "/".concat(splitUrl[splitUrl.length-2]).concat(splitUrl[splitUrl.length-1]);
        File file = new File(cache,fileName);
        try {
            FileInputStream  read = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(new BufferedInputStream(read));
            read.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void deleteFile(String url){
        String fileName = url.substring(url.lastIndexOf("/"));
        try{
            File file = new File(context.getCacheDir(),fileName);
            boolean isDelete =file.delete();
            Log.e("isDelete", Boolean.toString(isDelete));
        }
        catch (Exception e){
            Log.e("deleteFile",e.toString());
        }

    }



}
