package com.example.lenovo.mymovieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lenovo.mymovieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by lenovo on 23/03/2016.
 */
public class ImageAdapter extends BaseAdapter {
    String[] posters;
    Context myContext;


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
        ImageView image;
        if (convertView == null) {
//            image = new ImageView(myContext);
//            image.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            image.setPadding(20,10,20,10);
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View imageView=inflater.inflate(R.layout.grid_item,null);
             image= (ImageView) imageView.findViewById(R.id.image_item);
        }
        else
        {
            image = (ImageView) convertView;
        }

        Picasso.with(myContext).load(posters[position]).into(image);
        return image;
    }

}
