package jthd.trumpeter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Jesse on 1/10/2016.
 */
public final class ImageManager {

    // TODO This should only be loaded once and be freely used when asynchronously loading images later on. Make certain this is the case.
    private final static Bitmap optimizedPlaceHolderImage = ImageManager.decodeSampledBitmapFromResource(App.getAppContext().getResources(), R.drawable.default_profile_picture, 60, 60);

    private ImageManager(){

    }


    public static void loadBitmap(int resId, ImageView imageView, int x, int y) {
        if (BitmapWorkerTask.cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, x, y);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(App.getAppContext().getResources(), optimizedPlaceHolderImage, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight){

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // TODO assuming that decodeByteArray fills options with data as decodeResource does
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }




}
