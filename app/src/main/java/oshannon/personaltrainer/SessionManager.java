package oshannon.personaltrainer;

import oshannon.personaltrainer.Utils.MediaUtils;
import oshannon.personaltrainer.db.AngleDbHelper;
import oshannon.personaltrainer.db.DbHelper;
import oshannon.personaltrainer.db.SessionDbHelper;
import oshannon.personaltrainer.models.Angle;
import oshannon.personaltrainer.models.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by orrieshannon on 2014-09-22.
 */
public class SessionManager {

    ArrayList<SessionsListener> listeners;

    public enum SessionStatus{
        UNSENT(1), INREVIEW(2), REVIEWED(3);

        private int val;
        SessionStatus(int i) {
            val = i;
        }
        public int getVal() {
            return val;
        }
        public static SessionStatus fromVal(int val) {
            switch (val) {
                case 1:
                    return UNSENT;
                case 2:
                    return INREVIEW;
                case 3:
                    return REVIEWED;
                default:
                    return UNSENT;
            }
        }
    }

    private static SessionManager instance;

    private SessionManager() {
        listeners = new ArrayList<SessionsListener>();
    }

    public void addSessionsListener(SessionsListener listener) {
        this.listeners.add(listener);
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void createNewSession(Angle angle) {
        Date date = Calendar.getInstance().getTime();
        ArrayList<Angle> angles = new ArrayList<Angle>();
        angles.add(angle);
        Session session = new Session(date, null, SessionStatus.UNSENT, angles);
        long sessionId = SessionDbHelper.getInstance().addSession(session);
        session.setId(sessionId);
        angle.setSessionId(sessionId);
        AngleDbHelper.getInstance().addAngle(angle);
        session.addAngle(angle);

        for(SessionsListener listener : listeners) {
            listener.sessionAdded(session);
        }
    }

    /**
     * Gets all sessions
     */
    public ArrayList<Session> getSessions() {
        ArrayList<Session> sessions = SessionDbHelper.getInstance().getSessions();
        for(Session session : sessions) {
            ArrayList<Angle> angles = AngleDbHelper.getInstance().getAnglesForSession(session.getId());
            session.setAngles(angles);
        }
        return sessions;
    }

    /**
     * Gets single specified session with angles
     */
    public Session getSession(long id) {
        Session session = SessionDbHelper.getInstance().getSession(id);
        ArrayList<Angle> angles = AngleDbHelper.getInstance().getAnglesForSession(id);
        session.setAngles(angles);
        return session;
    }

    public interface SessionsListener {
        public void sessionAdded(Session session);
        public void sessionDeleted(Session session);
        public void sessionUpdated(Session session);
    }

    public void deleteSession(Session session) {
        ArrayList<Angle> angles = session.getAngles();
        for (Angle angle : angles) {
            MediaUtils.deleteMedia(angle.getThumbnailPath());
            MediaUtils.deleteMedia(angle.getVideoPath());
            AngleDbHelper.getInstance().deleteAngle(angle.getId());
        }
        int deleted = SessionDbHelper.getInstance().deleteSession(session.getId());
        for (SessionsListener listener : listeners) {
            listener.sessionDeleted(session);
        }
        DbHelper.getInstance().printContents(SessionDbHelper.TABLE_NAME);
        DbHelper.getInstance().printContents(AngleDbHelper.TABLE_NAME);
    }
}
