package com.example.root.moviedatabaseapp.presenter.base;

public interface Presenter<V> {

    void onViewAttached(V view);

    void onViewDetached();

    void onDestroyed();

}
