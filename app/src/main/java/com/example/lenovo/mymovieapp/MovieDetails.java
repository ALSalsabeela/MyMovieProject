package com.example.lenovo.mymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
//        Bundle extras = intent.getBundleExtra("mymovie");
//        movie= (Movie) extras.getSerializable("movie");
//        Bundle extras = getIntent().getBundleExtra("movie");
//        if (null == savedInstanceState) {
//            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
//            movieDetailsFragment.setArguments(extras);
//            getSupportFragmentManager().beginTransaction().add(R.id.activity_movie, movieDetailsFragment,"o").commit();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(MovieDetails.this,MySetting.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
