package com.example.root.moviedatabaseapp.presenter.factory;

import com.example.root.moviedatabaseapp.presenter.MovieDetailsPresenter;
import com.example.root.moviedatabaseapp.presenter.base.PresenterFactory;

public class MovieDetailsPresenterFactory implements PresenterFactory<MovieDetailsPresenter> {
    @Override
    public MovieDetailsPresenter create() {
        return new MovieDetailsPresenter();
    }
}
