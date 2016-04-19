package com.example.lenovo.mymovieapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.lenovo.mymovieapp.Models.Movie;
public class MainActivity extends AppCompatActivity implements DetailInterface{
   boolean isTablet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout panel2= (FrameLayout) findViewById(R.id.panel2);
        if(null==panel2){
            isTablet=false;
        }else{
           isTablet=true;
        }}
        @Override
    public void openDetails(Movie m) {
        if(isTablet){
            MovieDetailsFragment movieDetailsFragment= new MovieDetailsFragment();
            Bundle b= new Bundle();
            b.putSerializable("movie", m);
            movieDetailsFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.panel2,movieDetailsFragment).commit(); }
        else{
            Bundle b = new Bundle();
            b.putSerializable("movie", m);
            Intent intent = new Intent(MainActivity.this, MovieDetails.class);
            intent.putExtra("mymovie", b);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent=new Intent(MainActivity.this,MySetting.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
