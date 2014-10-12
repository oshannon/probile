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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by orrieshannon on 2014-09-28.
 */
public class MediaUtils {

    private static final String LOG_TAG = MediaUtils.class.getSimpleName();

    public enum MediaType {
        VIDEO, THUMBNAIL;
    }

    public static Bitmap createImageThumbnailFromVideo(String path) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
        return thumb;
    }

    /**
     * Saves an image to the internal directory
     * @param bitmapImage Image to save
     * @return
     */
    public static boolean saveImage(Bitmap bitmapImage, String path){
        // Create imageDir
        File file = new File(path);

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Loads an image from either internal or external directory
     * @return
     */
    public static Bitmap loadImage(String path, boolean internal)
    {
        Bitmap bmp = null;


        try {
            File f = new File(path);
            bmp = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static boolean deleteMedia(String filename) {
        File f = new File(filename);
        return f.delete();
    }

    public static String getMediaFileName(String filename, MediaType type) {
        String dir = null;
        String typeInPath = null;
        if (type == MediaType.VIDEO) {
            dir = MediaUtils.getExternalMediaDir().toString();
            typeInPath = ".mp4";
        } else {
            dir = MediaUtils.getInternalMediaDir().toString();
            typeInPath = ".png";
        }
        return dir + File.separator + filename + typeInPath;
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
