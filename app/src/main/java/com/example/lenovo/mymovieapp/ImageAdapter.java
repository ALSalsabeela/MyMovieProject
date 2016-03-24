package com.example.lenovo.mymovieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by lenovo on 23/03/2016.
 */
public class ImageAdapter extends BaseAdapter {
    String[] posters;
    Context myContext;
    ImageView image;

    public ImageAdapter(Context context, String[] l) {
        myContext=context;
        posters=l;
    }


    @Override
    public int getCount() {
        return posters.length;
    }

    @Override
    public Object getItem(int position) {
        return posters[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            image = new ImageView(myContext);
            image.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image.setPadding(20,10,20,10);
        }
        else
        {
            image = (ImageView) convertView;
        }
        Picasso.with(myContext).load(posters[position]).into(image);
        return image;
    }

}
