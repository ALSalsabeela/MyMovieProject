package com.example.lenovo.mymovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.mymovieapp.Models.Trailer;
import com.example.lenovo.mymovieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lenovo on 17/04/2016.
 */
public class TrailersAdapter extends BaseAdapter {
    Context myContext;
    ArrayList<Trailer>trailersArray;
    String key;
    public TrailersAdapter(Context c,ArrayList<Trailer> list){
        myContext=c;
        trailersArray=list;
    }
    @Override
    public int getCount() {
        return trailersArray.size();
    }

    @Override
    public Object getItem(int position) {
        return trailersArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.trailer_item, parent, false);

        ImageView image = (ImageView) view.findViewById(R.id.trailer_img);
        TextView trailersName = (TextView) view.findViewById(R.id.trailer_name);
        String base="http://img.youtube.com/vi/";
         key=trailersArray.get(position).getKey();
        String extension="/default.jpg";
        Picasso.with(myContext).load(base+key+extension).into(image);
        trailersName.setText(trailersArray.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="https://www.youtube.com/watch?v="+key;
                myContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        return view;

    }
}
