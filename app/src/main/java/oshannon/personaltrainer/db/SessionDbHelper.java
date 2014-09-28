package oshannon.personaltrainer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import oshannon.personaltrainer.SessionManager;
import oshannon.personaltrainer.models.Session;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by orrieshannon on 2014-09-22.
 */
public class SessionDbHelper {

    private DbHelper dbHelper;
    private static SessionDbHelper instance;

    public static final String TABLE_NAME = "sessions";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_NOTES = "notes";
    public static final String COLUMN_NAME_STATUS = "status";

    public static SessionDbHelper getInstance() {
        if (instance == null) {
            instance = new SessionDbHelper();
        }
        return instance;
    }

    private SessionDbHelper() {
        dbHelper = DbHelper.getInstance();
    }

    public long addSession(Session session) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DATE, session.getDate().toString());
        values.put(COLUMN_NAME_NOTES, session.getNotes());
        values.put(COLUMN_NAME_STATUS, session.getStatus().getVal());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = dbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        return newRowId;
    }

    public ArrayList<Session> getSessions() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        return sessionsFromCursor(cursor);
    }

    public Session getSession(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        return sessionsFromCursor(cursor).get(0);
    }

    private ArrayList<Session> sessionsFromCursor(Cursor cursor) {
        ArrayList<Session> sessions = new ArrayList<Session>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i ++) {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
                Date date = new Date(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
                String notes = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NOTES));
                int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
                sessions.add(new Session(id, date, notes, SessionManager.SessionStatus.fromVal(status)));
            }
        }
        cursor.close();
        return sessions;
    }

    private void deleteSession(long id) {
        String selection = COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        dbHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Update session
     * @param session Session to update
     * @param values Values to update. E.g.
     *               ContentValues values = new ContentValues();
     *               values.put(COLUMN_NAME_NOTES, "New note");
     */
    private void updateSession(Session session, ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(session.getId()) };
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

}
