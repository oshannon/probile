package oshannon.personaltrainer.models;

import android.graphics.Bitmap;
import android.net.Uri;
import oshannon.personaltrainer.Utils.MediaUtils;

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
    private String thumbnailPath;
    private String videoPath;

    /**
     * Constructor when creating a new Angle and we don't have the ID (Db creates this)
     */
    public Angle(Uri uri){
        String path = uri.getPath();

        // In order to get the filename we have to strip off the path and the .mp4 from the end
        filename = path.substring(path.lastIndexOf(File.separator) + 1, path.length() - 4);

        createURIs();

        // Create a thumbnail using the video
        thumbnail = MediaUtils.createImageThumbnailFromVideo(getVideoPath());
        MediaUtils.saveImage(thumbnail, thumbnailPath);
    }

    /**
     * Constructor used when recreating the angle from the db and the id is known
     */
    public Angle(long id, long sessionId, String filename, String notes){
        this.id = id;
        this.sessionId = sessionId;
        this.notes = notes;
        this.filename = filename;
        createURIs();
    }

    private void createURIs() {
        videoPath = MediaUtils.getMediaFileName(filename, MediaUtils.MediaType.VIDEO);
        thumbnailPath = MediaUtils.getMediaFileName(filename, MediaUtils.MediaType.THUMBNAIL);
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
            thumbnail = MediaUtils.loadImage(thumbnailPath, true);
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
        File mediaFile = new File(MediaUtils.getMediaFileName(timeStamp, MediaUtils.MediaType.VIDEO));

        return mediaFile;
    };

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getFilename() {
        return filename;
    }
}
