package com.example.root.moviedatabaseapp.repository;

import android.content.Context;


import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.moviedatabaseapp.model.Movie;
import com.example.root.moviedatabaseapp.model.MovieListResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularMoviesLoader extends MovieLoader {




    protected OnError onError;


    protected Context context;

    protected int page;
    protected OnListResult onListResult;
    public PopularMoviesLoader(Context context, int page, OnListResult onListResult,
                                OnError onError){
        super(context);
        this.context = context;

        this.page = page;
        this.onListResult = onListResult;
        this.onError = onError;
    }

    void parseJsonResult(JSONObject response){
        MovieListResult result = new MovieListResult();
        try {
            int page = response.getInt("page");
            int totalResults = response.getInt("total_results");
            int totalPages = response.getInt("total_pages");
            result.setPage(page);
            result.setTotalPages(totalPages);
            result.setTotalResults(totalResults);

            List<Movie> movies = new ArrayList<>();

            JSONArray array = response.getJSONArray("results");

            for (int i = 0; i < response.length(); i++) {

                JSONObject movieItem = array.getJSONObject(i);

                Movie movie = parseMovieInfo(movieItem,new Movie());

                movies.add(movie);
            }
            result.setResults(movies);

        }
        catch (JSONException e){
            onError.callback(e);
        }
        onListResult.callback(result);
    }

    private String buildRequest(int page){
       return host+"movie/popular?api_key="+apiKey+"&language=en-US&page="+page;
    }


    public void execute() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(context);
        String url =  buildRequest(page);

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
