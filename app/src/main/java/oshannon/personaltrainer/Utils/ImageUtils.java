package oshannon.personaltrainer.Utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import oshannon.personaltrainer.AppState;
import oshannon.personaltrainer.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by orrieshannon on 2014-09-28.
 */
public class ImageUtils {

    private static final String LOG_TAG = ImageUtils.class.getSimpleName();

    public static Bitmap createImageThumbnailFromVideo(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);

        return thumb;
    }

    public static String saveImageToInternalStorage(Bitmap bitmapImage, String path){
        File directory = getInternalMediaDir();
        // Create imageDir
        File file = new File(directory, path);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(file);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadImageFromInternalStorage(String filename)
    {
        Bitmap bmp = null;
        try {
            File f = new File(getInternalMediaDir(), filename);
            bmp = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static File getExternalMediaDir() {
        // TODO: To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppState.APPLICATION_NAME);

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(LOG_TAG, "failed to create directory");
                return null;
            }
        }
        return mediaStorageDir;
    }

    public static File getInternalMediaDir() {
        ContextWrapper cw = new ContextWrapper(AppState.getInstance().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        return directory;
    }
}
