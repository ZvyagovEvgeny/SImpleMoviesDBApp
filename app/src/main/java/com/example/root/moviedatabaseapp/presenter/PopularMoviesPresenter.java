package com.example.root.moviedatabaseapp.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.example.root.moviedatabaseapp.activity.PopularMoviesView;
import com.example.root.moviedatabaseapp.adapter.PopularMoviesListAdapter;
import com.example.root.moviedatabaseapp.model.Movie;
import com.example.root.moviedatabaseapp.model.MovieListResult;
import com.example.root.moviedatabaseapp.presenter.base.Presenter;
import com.example.root.moviedatabaseapp.repository.PopularMoviesLoader;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesPresenter implements Presenter<PopularMoviesView> {

    private PopularMoviesView popularMoviesView;
    private PopularMoviesListAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    private int countPagesDownloaded = 0;
    private boolean downloadStarted;


    public void onItemClicked(int position){
        Movie movie = movieList.get(position);
        if(movie!=null){
            popularMoviesView.startMovieDetailActivity(movie.getId());
        }
    }

    private void setDownloadStarted(boolean started){
        downloadStarted = started;
        if(popularMoviesView!=null){
            popularMoviesView.showProgressBar(started);
        }
    }

    private void startDownloadNewPage(){
        int pageToDownload = countPagesDownloaded + 1;
        if(popularMoviesView==null)return;
        PopularMoviesLoader loader = new PopularMoviesLoader(popularMoviesView.getContext(),
                pageToDownload,this::onResult,this::onError);
        loader.execute();
        setDownloadStarted(true);
    }

    public void download(){
        if(downloadStarted) return;
        startDownloadNewPage();
    }

    private void onResult(MovieListResult result){
        int page = result.getPage();
        movieList.addAll(result.getResults());
        countPagesDownloaded = page;

        if(adapter!=null)
            adapter.notifyDataSetChanged();
        setDownloadStarted(false);
    }

    private void onError(Throwable t){
        if(popularMoviesView!=null)
            popularMoviesView.showMessage(t.getMessage());
    }

    @Override
    public void onViewAttached(PopularMoviesView view) {
        popularMoviesView = view;
        adapter = new PopularMoviesListAdapter(view.getContext(),movieList);
        if(countPagesDownloaded==0)
            startDownloadNewPage();
        view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewDetached() {
        popularMoviesView = null;
    }

    @Override
    public void onDestroyed() {

    }

}
