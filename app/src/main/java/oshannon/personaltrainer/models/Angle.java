package oshannon.personaltrainer.models;

import android.graphics.Bitmap;
import android.net.Uri;
import oshannon.personaltrainer.Utils.ImageUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by orrieshannon on 2014-09-26.
 */
public class Angle {

    private long id;
    private long sessionId;

    private String filename;
    private String notes;
    private Bitmap thumbnail;

    /**
     * Constructor when creating a new Angle and we don't have the ID (Db creates this)
     */
    public Angle(Uri uri){
        String fullPath = uri.getPath();

        // In order to get the filename we have to strip off the path and the .mp4 from the end
        filename = fullPath.substring(fullPath.lastIndexOf(File.separator) + 1, fullPath.length() - 4);

        // Create a thumbnail using the video
        thumbnail = ImageUtils.createImageThumbnailFromVideo(fullPath);
        ImageUtils.saveImageToInternalStorage(thumbnail, filename);
    }

    /**
     * Constructor used when recreating the angle from the db and the id is known
     */
    public Angle(long id, long sessionId, String filename, String notes){
        this.id = id;
        this.sessionId = sessionId;
        this.notes = notes;
        this.filename = filename;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getId() {
        return id;
    }

    // TODO: use callback to lazy load
    public Bitmap getThumbnail() {
        if (thumbnail == null) {
            thumbnail = ImageUtils.loadImageFromInternalStorage(getThumbnailPath());
        }
        return thumbnail;
    }

    /**
     * Used to create a new file for a new angle video
     * @return
     */
    public static File getNewVideoFile(){
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(getMediaFilePath(timeStamp, MediaType.VIDEO));

        return mediaFile;
    };

    private enum MediaType {
        VIDEO, THUMBNAIL;
    }

    private static String getMediaFilePath(String filename, MediaType type) {
        String dir = null;
        String typeInPath = null;
        if (type == MediaType.VIDEO) {
            dir = ImageUtils.getExternalMediaDir().toString();
            typeInPath = ".mp4";
        } else {
            dir = ImageUtils.getInternalMediaDir().toString();
            typeInPath = ".png";
        }
        return dir + File.separator + filename + ".mp4";
    }

    public String getVideoPath() {
        return getMediaFilePath(filename, MediaType.VIDEO);
    }

    public String getThumbnailPath() {
        return getMediaFilePath(filename, MediaType.THUMBNAIL);
    }

    public String getFilename() {
        return filename;
    }
}
