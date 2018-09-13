package com.example.root.moviedatabaseapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.adapter.ImageGridViewAdapter;
import com.example.root.moviedatabaseapp.presenter.MovieDetailsPresenter;
import com.example.root.moviedatabaseapp.presenter.base.PresenterFactory;
import com.example.root.moviedatabaseapp.presenter.factory.MovieDetailsPresenterFactory;
import com.example.root.moviedatabaseapp.utils.ImageDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDetailsActivity
        extends BasePresenterActivity<MovieDetailsPresenter,MovieDetailsView>
        implements MovieDetailsView{


    static final String MOVIE_IMAGE = "movieImage";
    static final int COUNT_IMAGES = 4;
    static final int IMAGE_SPACING = 5;


    String tag = "MovieDetailsActivity";
    MovieDetailsPresenter presenter;

    private int movieId;

    private TextView title;
    private TextView originalTitle;
    private TextView countries;
    private TextView genres;
    private TextView movieTagLine;
    private ImageView poster;
    private TextView movieOverview;
    private TextView score;
    private GridView movieImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        movieId = intent.getIntExtra("id",-1);
        title = findViewById(R.id.movieTitleDetailView);
        originalTitle = findViewById(R.id.originalTitleDetailView);
        countries = findViewById(R.id.countriesTVDetail);
        genres = findViewById(R.id.genresDetailView);
        movieTagLine = findViewById(R.id.tagLineDetailView);
        poster = findViewById(R.id.posterViewDetailView);
        movieOverview = findViewById(R.id.movieOverview);
        score = findViewById(R.id.avgScoreDetailView);
        movieImages = findViewById(R.id.imagesGV);
    }

    @NonNull
    @Override
    protected String tag() {
        return tag;
    }

    @NonNull
    @Override
    protected PresenterFactory<MovieDetailsPresenter> getPresenterFactory() {
        return new MovieDetailsPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull MovieDetailsPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void setAdapter(BaseAdapter adapter) {
        movieImages.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(boolean show) {

    }

    @Override
    public int getMovieId() {
        return movieId;
    }

    @Override
    public void setMovieTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setMovieOriginalTitle(String title) {
        originalTitle.setText(title);
    }

    @Override
    public void setMovieGenres(List<String> genres) {
        String result = "";
        for(String g:genres){
            result = result + g+" ";
        }
        this.genres.setText(result);
    }

    @Override
    public void setPoster(String url) {
        ImageDownloader.downloadImage(url,poster);
    }

    @Override
    public void setOverview(String overview) {
        this.movieOverview.setText(overview);
    }

    @Override
    public void setCountries(List<String> names) {
        String result = "";
        for(String g:names){
            result = result + g+" ";
        }
        countries.setText(result);
    }

    @Override
    public void setMovieTag(String tag) {
        movieTagLine.setText("\""+tag+"\"");
    }

    @Override
    public void setScore(double score) {
        this.score.setText(String.valueOf(score));
    }

    public void setMovieImagesToGrid(List<String> images){
        String [] from = {MOVIE_IMAGE};
        int [] to = {R.id.imageViewGridViewItem};
        List<Map<String, String>> data = new ArrayList<>();

        for(String path: images){
            Map<String,String> values = new HashMap<>();
            values.put(MOVIE_IMAGE,path);
            data.add(values);
        }
        ImageGridViewAdapter imageGridViewAdapter =
                new ImageGridViewAdapter(this,data,R.layout.image_grid_view_item,from,to);

        movieImages.setAdapter(imageGridViewAdapter);
        adjustGridView();
    }

    private void adjustGridView() {
        movieImages.setNumColumns(COUNT_IMAGES);
        movieImages.setVerticalSpacing(0);
        movieImages.setHorizontalSpacing(IMAGE_SPACING);
    }

}
