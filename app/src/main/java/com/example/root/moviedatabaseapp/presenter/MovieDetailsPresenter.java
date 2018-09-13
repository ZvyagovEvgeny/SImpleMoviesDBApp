package com.example.root.moviedatabaseapp.presenter;

import com.example.root.moviedatabaseapp.activity.MovieDetailsView;
import com.example.root.moviedatabaseapp.model.Country;
import com.example.root.moviedatabaseapp.model.Genre;
import com.example.root.moviedatabaseapp.model.Movie;
import com.example.root.moviedatabaseapp.model.MovieImage;
import com.example.root.moviedatabaseapp.presenter.base.Presenter;
import com.example.root.moviedatabaseapp.repository.MovieDetailsLoader;
import com.example.root.moviedatabaseapp.repository.MovieImagesLoader;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsPresenter implements Presenter<MovieDetailsView> {

    private static String imagePathPrefix = "https://image.tmdb.org/t/p/w500/";
    private MovieDetailsLoader movieDetailsLoader;
    private boolean detailsLoaded = false;
    private static final int MAX_COUNT_IMAGES_TO_DRAW = 4;

    private MovieDetailsView movieDetailsView;

    private Movie movie;
    private List<MovieImage> movieImages = new ArrayList<>();
    private int movieId;

    @Override
    public void onViewAttached(MovieDetailsView view) {
        movieDetailsView = view;
        if(detailsLoaded)return;
        movieId = movieDetailsView.getMovieId();
        movieDetailsLoader = new MovieDetailsLoader(
                view.getContext(),movieId,
                this::onMovieDetailLoad,
                this::onError);
        movieDetailsLoader.execute();

        startDownloadMovieImages(movieId);
    }

    private List<String> getGenres(List<Genre> genres){
        List<String> nameList = new ArrayList<>(genres.size());
        for(Genre genre:genres){
            nameList.add(genre.getName());
        }
        return nameList;
    }
    private List<String> getCountries(List<Country> countries){
        List<String> nameList = new ArrayList<>(countries.size());
        for(Country country:countries){
            nameList.add(country.getShortName());
        }
        return nameList;
    }

    private void updateVew(Movie movie){
        if(movieDetailsView==null)return;
        movieDetailsView.setMovieTitle(movie.getTitle());
        movieDetailsView.setMovieOriginalTitle(movie.getOrigTitle());
        movieDetailsView.setMovieGenres(getGenres(movie.getGenres()));
        movieDetailsView.setCountries(getCountries(movie.getCountries()));
        movieDetailsView.setPoster(imagePathPrefix+movie.getPosterPath());
        movieDetailsView.setScore(movie.getVoteAverage());
        movieDetailsView.setOverview(movie.getOverview());
        movieDetailsView.setMovieTag(movie.getTagLine());
    }

    private void startDownloadMovieImages(int id){
        MovieImagesLoader loader = new MovieImagesLoader(
                movieDetailsView.getContext(),
                id,
               this::onImagesListReceived,
               this::onError
        );
        loader.execute();
    }

    private List<String> getImagesPaths(List<MovieImage> images){
        List<String> paths = new ArrayList<>();
        for(MovieImage movieImage : images){
            String path = imagePathPrefix + movieImage.getPath();
            paths.add(path);
        }
        return paths;
    }


    private void showMovieImages(){
        List<String> paths = getImagesPaths(movieImages);
        int countToDraw;
        if(paths.size()<MAX_COUNT_IMAGES_TO_DRAW){
            countToDraw = paths.size();
        }
        else
            countToDraw = MAX_COUNT_IMAGES_TO_DRAW;
        paths = paths.subList(0,countToDraw);
        movieDetailsView.setMovieImagesToGrid(paths);
    }

    public void onImagesListReceived(List images){
       movieImages = images;
       showMovieImages();
    }

    private void onMovieDetailLoad(Movie movie){
        this.movie = movie;
        updateVew(movie);


    }
    private void onError(Throwable throwable){
        movieDetailsView.showMessage(throwable.getMessage());
    }

    @Override
    public void onViewDetached() {

    }

    @Override
    public void onDestroyed() {

    }
}
