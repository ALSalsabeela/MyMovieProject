package com.example.lenovo.mymovieapp;

import java.io.Serializable;

/**
 * Created by lenovo on 23/03/2016.
 */
public class Movie implements Serializable {
    String posterPath;
    String title ;
    String overview;
    String topRating;
    String releseDate;
  static String[]posters;

    public Movie() {

    }

    public static void setPosters(String []posterarray) {
      posters = posterarray;
    }

    public String[] getPosters() {
        return posters;
    }
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleseDate() {
        return releseDate;
    }

    public void setReleseDate(String releseDate) {
        this.releseDate = releseDate;
    }

    public String getTopRating() {
        return topRating;
    }

    public void setTopRating(String topRating) {
        this.topRating = topRating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
