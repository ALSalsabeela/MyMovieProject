package com.example.lenovo.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_movie_details, container, false);
        // recieve object from intent
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getBundleExtra("mymovie");
        Movie movie= (Movie) extras.getSerializable("movie");
        //
        ImageView imageView = (ImageView) root.findViewById(R.id.poster);
        Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
        TextView title= (TextView) root.findViewById(R.id.title);
        title.setText(movie.getTitle());
        TextView overview= (TextView) root.findViewById(R.id.overview);
        overview.append("\n"+movie.getOverview());
        TextView vote_count= (TextView) root.findViewById(R.id.vote_count);
        vote_count.append("\n"+movie.getTopRating());
        TextView release_date= (TextView) root.findViewById(R.id.release_date);
        release_date.append("\n"+movie.getReleseDate());

        return root;
    }
}
