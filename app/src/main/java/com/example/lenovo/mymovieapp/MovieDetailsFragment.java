package com.example.lenovo.mymovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.lenovo.mymovieapp.Models.Movie;
import com.example.lenovo.mymovieapp.Models.Review;
import com.example.lenovo.mymovieapp.Models.Trailer;
import com.example.lenovo.mymovieapp.adapters.ReviewsAdapter;
import com.example.lenovo.mymovieapp.adapters.TrailersAdapter;
import com.example.lenovo.mymovieapp.database.DBhelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    LinearLayout trailersListView;
    LinearLayout reviewsListView;
    int id;
    ToggleButton favorite;
    ReviewsAdapter reviewsAdapter;
    TrailersAdapter trailerAdapter;
    Movie movie;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);
//        // recieve object from intent
//        Intent intent = getActivity().getIntent();
//        Bundle extras = intent.getBundleExtra("mymovie");
//        movie= (Movie) extras.getSerializable("movie");
        //      String mParam1 = getArguments().getString("param1");

        // Check if the screen width more that 600dp so if true it will be tablet
        Configuration configuration = getResources().getConfiguration();

        if (configuration.smallestScreenWidthDp >= 600) {
            Bundle b = getArguments();
            movie = (Movie) b.getSerializable("movie");
        } else {
            // Receiving the Serializable directly from the MainActivity.
            Intent intent = getActivity().getIntent();
            if (intent.hasExtra("movie")) {
                if (intent.getExtras().get("movie") != null) {
                    movie = (Movie) intent.getExtras().getSerializable("movie");
                }
            }
        }

        if (movie != null) {
            id = movie.getId();
            ImageView imageView = (ImageView) root.findViewById(R.id.poster);
            Picasso.with(getContext()).load(movie.getPosterPath()).into(imageView);
            TextView title = (TextView) root.findViewById(R.id.title);
            title.setText(movie.getTitle());
            TextView overview = (TextView) root.findViewById(R.id.overview);
            overview.append("\n" + movie.getOverview());
            TextView vote_count = (TextView) root.findViewById(R.id.vote_count);
            vote_count.append(movie.getTopRating());
            TextView release_date = (TextView) root.findViewById(R.id.release_date);
            release_date.append(movie.getReleseDate());
            reviewsListView = (LinearLayout) root.findViewById(R.id.review_list);
            trailersListView = (LinearLayout) root.findViewById(R.id.trailer_list);
            update();
        }

        favorite = (ToggleButton) root.findViewById(R.id.favorite_button);
        final SharedPreferences prefs = getContext().getSharedPreferences("favorite", 0);
        final SharedPreferences.Editor e = prefs.edit();
        favorite.setChecked(prefs.getBoolean("checked" + movie.getId(), false));
        favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    DBhelper helper = new DBhelper(getContext());
                    long id = helper.add_To_FAVORITE(movie);
                    e.putBoolean("checked" + movie.getId(), favorite.isChecked());
                    e.commit();
                    Toast.makeText(getContext(), "id =" + id + prefs.getBoolean("checked", true), Toast.LENGTH_LONG).show();
                } else {
                    // The toggle is disabled
                    DBhelper helper = new DBhelper(getContext());
                    long id = helper.deleteFromFavorite(movie.getId() + "");
                    e.putBoolean("checked" + movie.getId(), favorite.isChecked());
                    e.commit();
                    Toast.makeText(getContext(), "id =" + prefs.getBoolean("checked", false), Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    private void update() {
        connectReview connectReview = new connectReview();
        connectReview.execute();
        connectTrailer connectTrailer = new connectTrailer();
        connectTrailer.execute();

    }

    class connectReview extends AsyncTask<Void, Integer, ArrayList<Review>> {

        @Override
        protected ArrayList<Review> doInBackground(Void... params) {
            //connection to movie api
            //    http://api.themoviedb.org/3/movie/209112/reviews?api_key=fbd50b883df0d6de3344f4566e09783c
            //    http://api.themoviedb.org/3/movie/209112/videos?api_key=fbd50b883df0d6de3344f4566e09783c
            //    http://api.themoviedb.org/3/movie/popular?api_key=fbd50b883df0d6de3344f4566e09783c
            String action = "reviews";
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String data = null;

            try {
                // uri bulider
                String movie_param = "movie";
                String baseUrl = "http://api.themoviedb.org/3/";
                String apiKey = "api_key";
                String movieId = "" + id + "";
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendPath(movie_param).appendPath(movieId).appendEncodedPath(action)
                        .appendQueryParameter(apiKey, BuildConfig.MOVIES_API_KEY).build();
                URL url = new URL(builtUri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    data = null;
                }
                data = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                data = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

                try {
                    return getReviewsOfMovies(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> list) {
            reviewsAdapter = new ReviewsAdapter(getContext(), list);
            for (int i = 0; i < list.size(); i++) {
                View view = reviewsAdapter.getView(i, null, null);
                reviewsListView.addView(view);
            }
            reviewsAdapter.notifyDataSetChanged();

        }

        private ArrayList<Review> getReviewsOfMovies(String jsonResult) throws JSONException {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            ArrayList<Review> reviewsList = new ArrayList<Review>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String author = object.getString("author");
                String content = object.getString("content");
                Review review = new Review();
                review.setAuthor(author);
                review.setContent(content);
                reviewsList.add(review);
            }
            return reviewsList;
        }

    }

    class connectTrailer extends AsyncTask<Void, Integer, ArrayList<Trailer>> {

        @Override
        protected ArrayList<Trailer> doInBackground(Void... params) {
            //connection to movie api
            //    http://api.themoviedb.org/3/movie/209112/reviews?api_key=fbd50b883df0d6de3344f4566e09783c
            //    http://api.themoviedb.org/3/movie/209112/videos?api_key=fbd50b883df0d6de3344f4566e09783c
            //    http://api.themoviedb.org/3/movie/popular?api_key=fbd50b883df0d6de3344f4566e09783c
            String action = "videos";
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String data = null;

            try {
                // uri bulider
                String movie_param = "movie";
                String baseUrl = "http://api.themoviedb.org/3/";
                String apiKey = "api_key";
                String movieId = "" + id + "";
                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendPath(movie_param).appendPath(movieId).appendEncodedPath(action)
                        .appendQueryParameter(apiKey, BuildConfig.MOVIES_API_KEY).build();
                URL url = new URL(builtUri.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream input = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    data = null;
                }
                reader = new BufferedReader(new InputStreamReader(input));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    data = null;
                }
                data = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                data = null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

                try {
                    return getMoviesTrailers(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> list) {
            //trailerAdapter=new TrailersAdapter(getContext(),list);
            // trailersListView.setAdapter(trailerAdapter);
            trailerAdapter = new TrailersAdapter(getContext(), list);
            for (int i = 0; i < list.size(); i++) {
                View view = trailerAdapter.getView(i, null, null);
                trailersListView.addView(view);
            }
            trailerAdapter.notifyDataSetChanged();
        }

    }

    private ArrayList<Trailer> getMoviesTrailers(String jsonResult) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResult);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        ArrayList<Trailer> trailersList = new ArrayList<Trailer>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String name = object.getString("name");
            String key = object.getString("key");
            Trailer trailer = new Trailer();
            trailer.setName(name);
            trailer.setKey(key);
            trailersList.add(trailer);
        }
        return trailersList;
    }

}


