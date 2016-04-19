package com.example.lenovo.mymovieapp;

import android.content.Context;
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
import android.widget.Toast;

import com.example.lenovo.mymovieapp.Models.Movie;
import com.example.lenovo.mymovieapp.adapters.ImageAdapter;
import com.example.lenovo.mymovieapp.database.DBhelper;

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
public class MainActivityFragment extends Fragment {
      GridView moviesGraid;
      ArrayList<Movie> moviesList;
      ImageAdapter adapter;
      DetailInterface interfase;

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
                interfase.openDetails(movie);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        interfase= (DetailInterface) getActivity();
    }

//    public void setListener(DetailInterface inter){
//          interfase=inter;
//      }
       private void update(){
           Connection con=new Connection();
            con.execute();
       }

    private class Connection extends AsyncTask<Void,Integer,ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            // get setting
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key)
                    , getString((R.string.pref_sort_order_highest_rated)));

            if (sortOrder.equals(getString(R.string.pref_sort_order_favorite))) {
                DBhelper helper=new DBhelper(getContext());
                //    moviesList.clear();
                moviesList=helper.get_FavoriteMovie();
                if(moviesList==null){
                    Toast.makeText(getContext(),"no favorite films",Toast.LENGTH_LONG).show();
                    return null;
                }
                else{
                return moviesList;
            }}

            else{
                //connection to movie api
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String data = null;

                try {
                // uri bulider
                String type = sortOrder;

                    String movie_param = "movie";
                    String baseUrl = "http://api.themoviedb.org/3/";
                    String apiKey = "api_key";
                    Uri builtUri = Uri.parse(baseUrl).buildUpon()
                            .appendPath(movie_param).appendEncodedPath(type)
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

                }catch(IOException e){
                    Log.e("PlaceholderFragment", "Error ", e);
                    data = null;
                }
                finally{
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
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            adapter=new ImageAdapter(getActivity(),getAllPosters(movies));
            moviesGraid.setAdapter(adapter);
        }

        //
        private void getDataFromJson(String data) throws JSONException {
            moviesList.clear();
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
               int id=object.getInt("id") ;
               movie=new Movie();
               movie.setPosterPath(posterPath);
               posters[i]=posterPath;
               movie.setTitle(title);
               movie.setId(id);
               movie.setTopRating(topRating);
               movie.setOverview(overview);
               movie.setReleseDate(releseDate);
               moviesList.add(movie);
           }
          // movie.setPosters(posters);
       }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public String[]getAllPosters(ArrayList<Movie>list){
        String[]posterspath=new String[list.size()];
         for(int i=0;i<list.size();i++){
             posterspath[i]=moviesList.get(i).getPosterPath();
            }
        return posterspath;
        }
    }

