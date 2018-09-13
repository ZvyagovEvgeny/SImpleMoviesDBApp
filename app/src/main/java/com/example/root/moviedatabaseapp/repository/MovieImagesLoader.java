package com.example.root.moviedatabaseapp.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.model.Movie;
import com.example.root.moviedatabaseapp.model.MovieImage;
import com.example.root.moviedatabaseapp.model.MovieListResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.root.moviedatabaseapp.utils.LogTag.LOG_TAG;

public class MovieImagesLoader extends MovieLoader{
    private MovieLoader.OnError onError;

    protected Context context;

    protected int movieId;
    private OnListReady onListReady;



    public MovieImagesLoader(Context context, int movieId,
                                OnListReady onListReady,
                                OnError onError){
        super(context);
        this.context = context;
        this.movieId = movieId;
        this.onListReady = onListReady;
        this.onError = onError;


    }

    private List<MovieImage> parseMovieImagesJSON(JSONObject object)throws JSONException{
        JSONArray array = object.getJSONArray("backdrops");
        List<MovieImage> movieImages  = new ArrayList<>();
        for(int i=0; i<array.length(); i++){
            JSONObject item = array.getJSONObject(i);
            String imagePath = item.getString("file_path");
            movieImages.add(new MovieImage(imagePath));
        }
        return movieImages;
    }

    void parseJsonResult(JSONObject response){
        try{
            List<MovieImage> images = parseMovieImagesJSON(response);
            onListReady.callback(images);

        }catch (JSONException e){
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    private String buildRequest(int id){
        return host+"movie/"+id+"/images?api_key="+apiKey;
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
        // Log.d(LOG_TAG, error.getMessage());
        onError.callback(error);
    }

}
