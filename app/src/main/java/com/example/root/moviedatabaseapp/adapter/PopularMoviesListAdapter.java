package com.example.root.moviedatabaseapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.moviedatabaseapp.utils.ImageDownloader;
import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.model.Movie;


import java.util.List;

public class PopularMoviesListAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    List<Movie> objects;
    static String imagePathPrefix = "https://image.tmdb.org/t/p/w500/";

    public PopularMoviesListAdapter(Context context, List<Movie> movies) {
        ctx = context;
        objects = movies;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }





    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view==null){
            view = lInflater.inflate(R.layout.movies_list_item,parent,false);
        }


        Movie movie = objects.get(position);

        ((TextView) view.findViewById(R.id.movieTitle)).setText(movie.getTitle());
        ((TextView) view.findViewById(R.id.originalTitle)).setText(movie.getOrigTitle());

        TextView score = view.findViewById(R.id.avgScore);
        if(movie.getVoteAverage()<5){
            score.setBackgroundColor(Color.RED);
        }
        if(movie.getVoteAverage()>=7.5){
            score.setBackgroundColor(Color.GREEN);
        }
        if(movie.getVoteAverage()>5 && movie.getVoteAverage()<7.5){
            score.setBackgroundColor(Color.YELLOW);
        }

        ((TextView) view.findViewById(R.id.avgScore)).setText(String.valueOf(movie.getVoteAverage()));

        ImageView imageView = view.findViewById(R.id.posterView);

        String path = imagePathPrefix + movie.getPosterPath();

        ImageDownloader executingTask = (ImageDownloader)imageView.getTag();
        if(executingTask!=null){
            executingTask.cancel(false);
        }
        ImageDownloader downloader = ImageDownloader.downloadImage(path,imageView);
        imageView.setTag(downloader);

        return view;

    }





}
