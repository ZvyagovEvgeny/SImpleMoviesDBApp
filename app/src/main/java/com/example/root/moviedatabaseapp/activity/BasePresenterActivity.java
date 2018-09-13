package com.example.root.moviedatabaseapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.root.moviedatabaseapp.presenter.base.Presenter;
import com.example.root.moviedatabaseapp.presenter.base.PresenterFactory;
import com.example.root.moviedatabaseapp.loader.PresenterLoader;
import com.example.root.moviedatabaseapp.utils.LogTag;

import static com.example.root.moviedatabaseapp.utils.LogTag.LOG_TAG;


public abstract class BasePresenterActivity<P extends Presenter<V>,V> extends AppCompatActivity {

    private static final int LOADER_ID = 101;
    private P presenter;

    private int loaderId(){
        return LOADER_ID;
    }

    static void log(String d,String method){
        Log.d(LOG_TAG,d+": "+method);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("onCreate","Presenter");
        super.onCreate(savedInstanceState);

        Loader<P> loader = LoaderManager.getInstance(this).getLoader(loaderId());

        //Loader<P> loader = getSupportLoaderManager().getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        // LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        //getSupportLoaderManager().initLoader(loaderId(), null, new LoaderManager.LoaderCallbacks<P>() {

        LoaderManager.getInstance(this).initLoader(loaderId(),null,callbacks);


    }

    LoaderManager.LoaderCallbacks<P> callbacks = new LoaderManager.LoaderCallbacks<P>(){

        @Override
        public final Loader<P> onCreateLoader(int id, Bundle args) {
        //    doSomesing(5);
            Log.i(LOG_TAG, "onCreateLoader");
            return new PresenterLoader<>(BasePresenterActivity.this,
                    getPresenterFactory(), tag());
        }

        @Override
        public final void onLoadFinished(Loader<P> loader, P presenter) {
            Log.i(LOG_TAG, "onLoadFinished");
            BasePresenterActivity.this.presenter = presenter;;
            onPresenterCreatedOrRestored(presenter);
            if(!presenterNotifyed){
                presenter.onViewAttached(getPresenterView());
                presenterNotifyed = true;
            }

        }

        @Override
        public final void onLoaderReset(Loader<P> loader) {
            Log.i(LOG_TAG, "onLoaderReset");
            BasePresenterActivity.this.presenter = null;
        }
    };


    boolean presenterNotifyed = false;
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart-" + tag());
        if(presenter!=null){
            presenter.onViewAttached(getPresenterView());
            presenterNotifyed = true;
        }
        else
            presenterNotifyed = false;

    }

    @Override
    protected void onStop() {
        if(presenter!=null)
            presenter.onViewDetached();
        super.onStop();
        Log.i(LOG_TAG, "onStop-" + tag());
    }

    /**
     * String tag use for log purposes.
     */
    @NonNull
    protected abstract String tag();

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link Presenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterCreatedOrRestored(@NonNull P presenter);

    /**
     * Override in case of fragment not implementing Presenter<View> interface
     */
    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }



}
