package com.example.root.moviedatabaseapp.presenter.factory;

import com.example.root.moviedatabaseapp.presenter.PopularMoviesPresenter;
import com.example.root.moviedatabaseapp.presenter.base.PresenterFactory;

public class PopularMoviesPresenterFactory implements PresenterFactory<PopularMoviesPresenter> {
    @Override
    public PopularMoviesPresenter create() {
        return new PopularMoviesPresenter();
    }
}
