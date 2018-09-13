package com.example.root.moviedatabaseapp.activity;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

public interface PopularMoviesView {

    void setAdapter(BaseAdapter adapter);
    Context getContext();
    void showMessage(String message);
    void showProgressBar(boolean show);



    void startMovieDetailActivity(int movieId);


}
