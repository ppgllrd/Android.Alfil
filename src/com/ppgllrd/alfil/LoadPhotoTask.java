package com.ppgllrd.alfil;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * Created by pepeg on 27/06/13.
 */
public class LoadPhotoTask extends AsyncTask<String, Void, Drawable> {
    private ImageView imageView;

    public LoadPhotoTask(ImageView iv) {
        imageView = iv;
    }

    protected Drawable doInBackground(String... params) {
        //Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Drawable drawable = null;
        if(isCancelled()) {
            return drawable;
        }

        drawable = Drawable.createFromPath(params[0]);
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        if(imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }
}