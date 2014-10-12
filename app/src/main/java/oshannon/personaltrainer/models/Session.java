package oshannon.personaltrainer.models;

import android.database.Cursor;
import oshannon.personaltrainer.SessionManager;
import oshannon.personaltrainer.db.SessionDbHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by orrieshannon on 2014-09-21.
 */
public class Session {
    private long id;
    private Date date;
    private String notes;
    private SessionManager.SessionStatus status;
    private ArrayList<Angle> angles;

    /**
     * This should only be used by the SessionDbAdapter
     */
    public Session(long id, Date date, String notes, SessionManager.SessionStatus status) {
        this(date, notes, status, null);
        this.id = id;
    }

    /**
     * Constructor used when SessionManager creates a new session
     */
    public Session(Date date, String notes, SessionManager.SessionStatus status, ArrayList<Angle> angles) {
        this.date = date;
        this.notes = notes;
        this.status = status;
        this.angles = angles;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SessionManager.SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionManager.SessionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Angle> getAngles() {
        return angles;
    }

    public void setAngles(ArrayList<Angle> angles) {
        this.angles = angles;
    }

    public void addAngle(Angle angle) {
        this.angles.add(angle);
    }
}
