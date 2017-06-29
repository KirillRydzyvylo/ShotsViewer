package com.dribbble.shotsviewer.loders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.dribbble.shotsviewer.loders.ImageCache;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import info.guardianproject.netcipher.NetCipher;

/**
 * Created by kirill on 17.06.17.
 */

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    private Context context;
    ImageView imageView;
       public ImageLoader(String url , Context context ,ImageView imageView) {
        this.url = url;
        this.context = context;
           this.imageView = imageView;
    }



    @Override
    protected Bitmap doInBackground(Void... params) {

        ImageCache imageCache = new ImageCache(context);
        if(imageCache.readImage(url) != null){
            Log.e("readFromCache","read");
            return imageCache.readImage(url);
        }
        else {
            Log.e("loadFromInternet","load");
            try {
                URL imageURL = new URL(url);

                HttpsURLConnection connection = NetCipher.getHttpsURLConnection(imageURL);
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("URLerror", e.toString());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOerror", e.toString());
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap != null){
            ImageCache cache = new ImageCache(context);
            imageView.setImageBitmap(bitmap);
                cache.saveImage(url,bitmap);
        }

    }
}
