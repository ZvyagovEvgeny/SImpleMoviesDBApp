package com.example.root.moviedatabaseapp.presenter.base;


public interface PresenterFactory<T extends Presenter> {

    T create();

}
