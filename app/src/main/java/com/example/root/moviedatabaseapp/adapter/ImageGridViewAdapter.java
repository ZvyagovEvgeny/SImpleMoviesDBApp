package com.example.root.moviedatabaseapp.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.example.root.moviedatabaseapp.R;
import com.example.root.moviedatabaseapp.utils.ImageDownloader;

import java.util.List;
import java.util.Map;

public class ImageGridViewAdapter extends SimpleAdapter {

    private List<String> imageUrls;

    public ImageGridViewAdapter(Context context,
                                List<? extends Map<String, ?>> data,
                                int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public void setViewImage(ImageView v, String value) {
        if(v.getId() == R.id.imageViewGridViewItem){

            ImageDownloader imageDownloader = (ImageDownloader)v.getTag();
            if(imageDownloader!=null)
                imageDownloader.cancel(false);
            imageDownloader = ImageDownloader.downloadImage(value,v);
            v.setTag(imageDownloader);
        }
    }
}
