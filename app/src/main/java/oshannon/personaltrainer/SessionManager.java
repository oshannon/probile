package oshannon.personaltrainer;

import android.view.inputmethod.InputMethod;
import oshannon.personaltrainer.db.AngleDbHelper;
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

    ArrayList<SessionsCallback> callbacks;

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
        callbacks = new ArrayList<SessionsCallback>();
    }

    public void addSessionsCallback(SessionsCallback callback) {
        this.callbacks.add(callback);
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
        angle.setSessionId(sessionId);
        AngleDbHelper.getInstance().addAngle(angle);
        session.addAngle(angle);

        for(SessionsCallback callback : callbacks) {
            callback.sessionAdded(session);
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

    public interface SessionsCallback {
        public void sessionAdded(Session session);
        public void sessionDeleted();
        public void sessionUpdated();
    }
}
