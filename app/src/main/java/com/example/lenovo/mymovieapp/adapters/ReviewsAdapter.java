package com.example.lenovo.mymovieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.mymovieapp.Models.Review;
import com.example.lenovo.mymovieapp.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 17/04/2016.
 */
public class ReviewsAdapter extends BaseAdapter {
  Context myContext;
    ArrayList<Review>reviewsArray;
    public ReviewsAdapter(Context c,ArrayList<Review> arrayList) {
        myContext=c;
        reviewsArray=arrayList;
    }

    @Override
    public int getCount() {
        return reviewsArray.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewsArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_item, parent, false);
            TextView author = (TextView) view.findViewById(R.id.authorText);
            TextView content = (TextView) view.findViewById(R.id.contentText);
            author.setText("author " + reviewsArray.get(position).getAuthor());
            content.setText("content" + reviewsArray.get(position).getContent());
            return view;
}

}