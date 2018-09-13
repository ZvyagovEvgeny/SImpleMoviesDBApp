package com.example.root.moviedatabaseapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.presenter.PopularMoviesPresenter;

import com.example.root.moviedatabaseapp.presenter.base.PresenterFactory;
import com.example.root.moviedatabaseapp.presenter.factory.PopularMoviesPresenterFactory;

import java.util.List;

public class PopularMoviesActivity extends BasePresenterActivity<PopularMoviesPresenter,
        PopularMoviesView> implements
        PopularMoviesView,
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener{

    private PopularMoviesPresenter presenter;
    private String tag = "PopularMoviesActivity";

    ListView listView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        progressBar = findViewById(R.id.loadMoviesPB);
        listView = findViewById(R.id.popularMoviesLV);
        listView.setOnItemClickListener(this);

        listView.setOnScrollListener(this);
    }

    @NonNull
    @Override
    protected String tag() {
        return tag;
    }

    @NonNull
    @Override
    protected PresenterFactory<PopularMoviesPresenter> getPresenterFactory() {
        return new PopularMoviesPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull PopularMoviesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();;
    }

    @Override
    public void showProgressBar(boolean show) {
        if(show){
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }else
        {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    public void startMovieDetailActivity(int movieId) {
        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("id",movieId);
        startActivity(intent);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(presenter==null) return;
        if(visibleItemCount==totalItemCount){

            presenter.download();
        }
        int endItem = firstVisibleItem + visibleItemCount;
        if(endItem==totalItemCount){
            presenter.download();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(presenter!=null)
            presenter.onItemClicked(position);
    }
}
