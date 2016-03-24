package com.example.lenovo.mymovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
      GridView moviesGraid;
      List<Movie> moviesList;
      ImageAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_main, container, false);
        moviesGraid= (GridView) root.findViewById(R.id.moviesGrid);
        moviesList=new ArrayList<Movie>();
         update();


        moviesGraid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = moviesList.get(position);
                Bundle b = new Bundle();
                b.putSerializable("movie", movie);
                Intent intent = new Intent(getActivity(), MovieDetails.class);
                intent.putExtra("mymovie", b);
                startActivity(intent);
            }
        });
        return root;
    }

       private void update(){
           Connection con=new Connection();
            con.execute();
       }

    private class Connection extends AsyncTask<Void,Integer,List<Movie>>{

        @Override
        protected List<Movie> doInBackground(Void... params) {
            //connection to movie api
            HttpURLConnection connection=null;
            BufferedReader reader=null;
            String data=null;
            // get setting
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder=prefs.getString(getString(R.string.pref_sort_order_key)
                    ,getString((R.string.pref_sort_order_highest_rated)));
            try {
                // uri bulider
            String type=sortOrder;
            String movie_param = "movie";
            String baseUrl = "http://api.themoviedb.org/3/";
            String apiKey = "api_key";
            Uri builtUri = Uri.parse(baseUrl).buildUpon()
                    .appendPath(movie_param).appendEncodedPath(type)
                    .appendQueryParameter(apiKey, BuildConfig.MOVIES_API_KEY).build();
            URL url = new URL(builtUri.toString());
            connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input=connection.getInputStream();
            StringBuffer buffer=new StringBuffer();
                if (input== null) {
                    data = null;
                }
            reader=new BufferedReader(new InputStreamReader(input));
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if (buffer.length() == 0) {
                    data = null;
                }
                data=buffer.toString();

        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            data = null;
        }
            finally {
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
                    // fetch data
                 getDataFromJson(data);
                    return moviesList;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            Movie m=new Movie();
            adapter=new ImageAdapter(getActivity(),m.getPosters());
            moviesGraid.setAdapter(adapter);
        }

        //
        private void getDataFromJson(String data) throws JSONException {
           JSONObject jsonObject=new JSONObject(data);
           JSONArray jsonArray=jsonObject.getJSONArray("results");
           String baseUri = "http://image.tmdb.org/t/p/w185/";
           String[]posters=new String[jsonArray.length()];
            Movie movie = null;
            for(int i=0;i<jsonArray.length();i++){
               JSONObject object=jsonArray.getJSONObject(i);
               String posterPath = baseUri +object.getString("poster_path");
               String title = object.getString("original_title");
               String overview=object.getString("overview");
               String topRating=object.getString("vote_count");
               String releseDate=object.getString("release_date");
               movie=new Movie();
               movie.setPosterPath(posterPath);
               posters[i]=posterPath;
               movie.setTitle(title);
               movie.setTopRating(topRating);
               movie.setOverview(overview);
               movie.setReleseDate(releseDate);
               moviesList.add(movie);
           }
           movie.setPosters(posters);
       }
    }
}
