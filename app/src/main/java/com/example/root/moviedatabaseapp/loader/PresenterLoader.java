package com.example.root.moviedatabaseapp.loader;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;


import com.example.root.moviedatabaseapp.presenter.base.Presenter;
import com.example.root.moviedatabaseapp.presenter.base.PresenterFactory;

import static com.example.root.moviedatabaseapp.utils.LogTag.LOG_TAG;


public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private final PresenterFactory<T> factory;
    private final String tag;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }


    //При старте активити
    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading-" + tag);

        // if we already own a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    //Работа лоадера, вызывается при forceLoad()
    @Override
    protected void onForceLoad() {
        Log.i(LOG_TAG, "onForceLoad-" + tag);

        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
        Log.i(LOG_TAG, "deliverResult-" + tag);
    }

    //При остановке активити
    @Override
    protected void onStopLoading() {
        Log.i(LOG_TAG, "onStopLoading-" + tag);
    }

    @Override
    protected void onReset() {
        Log.i(LOG_TAG, "onReset-" + tag);
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }

    public T getPresenter() {
        return presenter;
    }
}
