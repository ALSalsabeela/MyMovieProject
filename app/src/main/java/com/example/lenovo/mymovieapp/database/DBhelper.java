package com.example.lenovo.mymovieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lenovo.mymovieapp.database.DBcontract.movies;
import com.example.lenovo.mymovieapp.Models.Movie;

import java.util.ArrayList;

/**
 * Created by lenovo on 18/04/2016.
 */
public class DBhelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE="movies";

    public DBhelper(Context context) {
    super(context, DATABASE, null, VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(DBcontract.movies.CREATE_TABLE_MOVIE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS " + DBcontract.movies.TABLE_NAME);
        onCreate(db);
    }

    public long add_To_FAVORITE(Movie movie){
       SQLiteDatabase db=getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(movies.COLUMN_POSTER_PATH, movie.getPosterPath());
        value.put(movies.COLUMN_OVERVIEW,movie.getOverview());
        value.put(movies.COLUMN_MOVIE_ID,movie.getId());
        value.put(movies.COLUMN_TITLE,movie.getTitle());
        value.put(movies.COLUMN_VOTE,movie.getTopRating());
        value.put(movies.COLUMN_RELEASE_DATE, movie.getReleseDate());
        long id=  db.insert(DBcontract.movies.TABLE_NAME,null,value);
        return id;
    }

    public ArrayList<Movie> get_FavoriteMovie(){
        SQLiteDatabase db=getReadableDatabase();
        ArrayList<Movie>movieList=new ArrayList<>();
        Cursor cursor=db.query(movies.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<String> posters =new ArrayList<>();
        if(cursor!=null){
            while (cursor.moveToNext()){
                Movie movie=new Movie();
                movie.setPosterPath(cursor.getString(1));
                movie.setOverview(cursor.getString(2));
                movie.setId(cursor.getInt(3));
                movie.setTitle(cursor.getString(4));
                movie.setTopRating(cursor.getString(5));
                movie.setReleseDate(cursor.getString(6));
                posters.add(cursor.getString(1));
                movieList.add(movie);

            }
        }

        return movieList;
    }

    public long deleteFromFavorite(String id){
       SQLiteDatabase db=getWritableDatabase();
       long deletedRow= db.delete(movies.TABLE_NAME, movies.COLUMN_MOVIE_ID + " =?", new String[]{id});
       return deletedRow;
    }

    public void delet(){
         SQLiteDatabase db=getWritableDatabase();
         db.delete(movies.TABLE_NAME,null,null);
    }

}
