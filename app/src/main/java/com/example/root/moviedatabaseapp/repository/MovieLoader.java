package com.example.root.moviedatabaseapp.repository;

import android.content.Context;

import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.model.Country;
import com.example.root.moviedatabaseapp.model.Genre;
import com.example.root.moviedatabaseapp.model.Movie;
import com.example.root.moviedatabaseapp.model.MovieListResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class MovieLoader {

    protected String host;

    protected String apiKey;
    protected Context context;
    protected MovieLoader(Context context){
        this.context = context;
        apiKey = context.getResources().getString(R.string.movie_db_api_key);
        host = context.getResources().getString(R.string.movie_db_host);
    }

    @FunctionalInterface
    public interface OnListReady{
        void callback(List<?> result);
    }

    @FunctionalInterface
    public interface OnListResult{
        void callback(MovieListResult result);
    }

    @FunctionalInterface
    public interface OnMovieResult{
        void callback(Movie result);
    }

    @FunctionalInterface
    public interface OnError{
        void callback(Throwable message);
    }

    public static Movie parseMovieInfo(JSONObject jsonObject, Movie movie) throws JSONException {
        int voteCount = jsonObject.getInt("vote_count");
        int id = jsonObject.getInt("id");
        boolean video = jsonObject.getBoolean("video");
        double voteAverage = jsonObject.getDouble("vote_average");
        String title = jsonObject.getString("title");
        String path = jsonObject.getString("poster_path");
        String origTitle = jsonObject.getString("original_title");
        String backdrop = jsonObject.getString("backdrop_path");;
        String overview = jsonObject.getString("overview");
        String releaseDate = jsonObject.getString("release_date");

      //  Movie movie = new Movie();

        movie.setVoteCount(voteCount);
        movie.setId(id);
        movie.setVideo(video);
        movie.setVoteAverage(voteAverage);
        movie.setTitle(title);
        movie.setPosterPath(path);
        movie.setOrigTitle(origTitle);
        movie.setBackgroundPath(backdrop);
        movie.setOverview(overview);
        movie.setReleaseDate(releaseDate);
        return movie;
    }

    public static Movie parseExtendedInfo(JSONObject jsonObject, Movie movie)
    throws JSONException{
        String tagline = jsonObject.getString("tagline");
        movie.setTagLine(tagline);
        movie.setGenres(parseGenreInfo(jsonObject));
        movie.setCountries(parseCountryInfo(jsonObject));
        return movie;
    }

    public static List<Genre> parseGenreInfo(JSONObject jsonObject)
            throws JSONException{

        List<Genre> list = new ArrayList<>();
        JSONArray genresArray = jsonObject.getJSONArray("genres");
        for(int i=0; i<genresArray.length();i++){
            JSONObject genreItem = genresArray.getJSONObject(i);
            Genre genre = new Genre();
            genre.setId(genreItem.getInt("id"));
            genre.setName(genreItem.getString("name"));
            list.add(genre);

        }
        return list;
    }

    public static List<Country> parseCountryInfo(JSONObject jsonObject)
            throws JSONException{

        List<Country> list = new ArrayList<>();
        JSONArray genresArray = jsonObject.getJSONArray("production_countries");
        for(int i=0; i<genresArray.length();i++){
            JSONObject countryItem = genresArray.getJSONObject(i);
            Country country = new Country();
            country.setShortName(countryItem.getString("iso_3166_1"));
            country.setFullName(countryItem.getString("name"));
            list.add(country);

        }
        return list;
    }

}
