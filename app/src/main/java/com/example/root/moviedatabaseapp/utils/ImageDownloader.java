package com.example.root.moviedatabaseapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.root.moviedatabaseapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private static final int MB = 1024;
    private static final int CACHE_SIZE_DIVIDER = 2;

    static LruCache<String,Bitmap> bitmaps;
    static public Bitmap getCachedImage(String url){
        return bitmaps.get(url);
    }

    private ImageView imageView;

    private boolean cancelled = false;

    private Throwable lastError;

    public Throwable getLastError() {
        return lastError;
    }

    static {
        final int maxMem = (int) (Runtime.getRuntime().maxMemory()/MB);
        final int cacheSize = maxMem / CACHE_SIZE_DIVIDER;
        Log.d(LogTag.LOG_TAG,"Cashe size: "+cacheSize);
        bitmaps = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getByteCount()/MB;
            }
        };
    }

    public static ImageDownloader downloadImage(String path,ImageView imageView){

        Bitmap bitmap = getCachedImage(path);
        boolean imageNotCached = (bitmap == null);

        if(imageNotCached){
            ImageDownloader downloader = new ImageDownloader(imageView);
            downloader.execute(path);
            return downloader;
        }
        else {
            setBitmapToImageView(bitmap,imageView);
        }
        return null;
    }

    private ImageDownloader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        startAnim(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];

        Bitmap bitmap = null;
        try {
            Thread.sleep(100);
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        }catch (MalformedURLException e){
            Log.e(LogTag.LOG_TAG, e.getMessage());
            lastError = e;
        }catch (IOException e) {
            Log.e(LogTag.LOG_TAG, e.getMessage());
            lastError = e;
        }catch (Exception e){
            Log.e(LogTag.LOG_TAG, e.getMessage());
            lastError = e;
        }

        if(bitmap!=null){
            bitmaps.put(url,bitmap);
        }
        return bitmap;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        cancelled = true;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if(cancelled)return;
        setBitmapToImageView(result,imageView);
    }

    private static void setBitmapToImageView(Bitmap bitmap, ImageView imageView){
        stopAnim(imageView);
        imageView.setImageBitmap(bitmap);
    }

    static private void startAnim(ImageView imageView){
        Animation animation = new RotateAnimation(0,360,
                (float)(imageView.getWidth()/2),
                (float)imageView.getHeight()/2);

        animation.setDuration(2000);
        animation.setRepeatCount(Animation.INFINITE);

        imageView.setImageResource(R.drawable.progress_image);
        imageView.setAnimation(animation);
    }

    static private void stopAnim(ImageView imageView){
        imageView.setAnimation(null);
    }
}

