package com.dribbble.shotsviewer.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.dribbble.shotsviewer.R;
import com.dribbble.shotsviewer.data.Shot;
import com.dribbble.shotsviewer.db.DataBase;
import com.dribbble.shotsviewer.loders.ImageLoader;
import com.dribbble.shotsviewer.loders.JSONLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final long MILLISECONDS_PER_DAY = 86400000;
    private ArrayList<Shot> shots =null;
    private int itemHeight;
    private Context context;
    private DataBase dataBase;
    public RecyclerViewAdapter(ArrayList<Shot> shots) {
        this.shots = shots;
    }

    public void update(ArrayList<Shot> shots){
        this.shots = shots;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        dataBase = new DataBase(context);
        View v  = LayoutInflater.from(context).inflate(R.layout.item_recycler_view, parent,false);
        itemHeight = parent.getHeight()/2;
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.frameLayout.setLayoutParams( new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        holder.setIsRecyclable(false);

        Shot shot = shots.get(position);
        ImageLoader imageLoader = new ImageLoader(shot.getImageURL(),context, holder.imageView);
        imageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        holder.title.setText(shot.getTitle());
        holder.description.setText(shot.getDescription());
        if((position+1) == shots.size() & dataBase.countData()< 50){
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd");
            JSONLoader jsonLoader = new JSONLoader(context,formatForDateNow.format((dataBase.getMinUnixDateFromDB()-MILLISECONDS_PER_DAY)),this);
            jsonLoader.execute();

        }
    }

    @Override
    public int getItemCount() {
        try {
            return shots.size();
        }
        catch (NullPointerException e){
            Log.e("NullPointerException"," NullPointerException in shots RecyclerViewAdapter ");
            return 0;
        }

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView description;
        public ImageView imageView;
        public FrameLayout frameLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            frameLayout = (FrameLayout)itemView.findViewById(R.id.frame_item);
            imageView = (ImageView) itemView.findViewById(R.id.image_item);
            title = (TextView)itemView.findViewById(R.id.title_item);
            description = (TextView)itemView.findViewById(R.id.description_item);

        }
    }

}
