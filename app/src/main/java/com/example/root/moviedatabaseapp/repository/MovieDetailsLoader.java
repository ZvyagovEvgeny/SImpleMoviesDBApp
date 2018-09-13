package com.example.root.moviedatabaseapp.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.root.moviedatabaseapp.utils.LogTag.LOG_TAG;

public class MovieDetailsLoader extends MovieLoader {

    private OnError onError;


    protected Context context;

    protected int movieId;
    private OnMovieResult onMovieResult;

    public MovieDetailsLoader(Context context, int movieId, OnMovieResult onMovieResult,
                              OnError onError){
        super(context);
        this.context = context;
        this.movieId = movieId;
        this.onMovieResult = onMovieResult;
        this.onError = onError;
    }

    void parseJsonResult(JSONObject response){
        try{
            Movie movie = parseMovieInfo(response,new Movie());
            parseExtendedInfo(response,movie);
            onMovieResult.callback(movie);
        }catch (JSONException e){
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    private String buildRequest(int id){
        return host+"movie/"+id +"?api_key="+apiKey+"&language=en-US";
    }


    public void execute() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(context);
        String url =  buildRequest(movieId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        this::parseJsonResult, this::onError);

        queue.add(jsonObjectRequest);
    }

    void onError(VolleyError error){
        if(onError==null)return;;
        onError.callback(error);
    }

}
