package com.example.root.moviedatabaseapp.activity;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public interface MovieDetailsView {

    void setAdapter(BaseAdapter adapter);
    Context getContext();
    void showMessage(String message);
    void showProgressBar(boolean show);
    int getMovieId();


    void setMovieTitle(String title);
    void setMovieOriginalTitle(String title);
    void setMovieGenres(List<String> genres);
    void setPoster(String url);
    void setOverview(String overview);
    void setCountries(List<String> names);
    void setMovieTag(String tag);
    void setScore(double score);
    void setMovieImagesToGrid(List<String> images);


}
