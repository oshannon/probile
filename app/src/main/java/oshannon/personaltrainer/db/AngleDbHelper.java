package oshannon.personaltrainer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import oshannon.personaltrainer.models.Angle;

import java.util.ArrayList;

/**
 * Created by orrieshannon on 2014-09-26.
 */
public class AngleDbHelper {

    private DbHelper dbHelper;
    private static AngleDbHelper instance;

    public static final String TABLE_NAME = "angles";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_SESSION_ID = "sessionId";
    public static final String COLUMN_NAME_NOTES = "notes";
    public static final String COLUMN_NAME_FILENAME = "filename";

    public static AngleDbHelper getInstance() {
        if (instance == null) {
            instance = new AngleDbHelper();
        }
        return instance;
    }

    private AngleDbHelper() {
        dbHelper = DbHelper.getInstance();
    }

    public long addAngle(Angle angle) {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_SESSION_ID, angle.getSessionId());
        values.put(COLUMN_NAME_NOTES, angle.getNotes());
        values.put(COLUMN_NAME_FILENAME, angle.getFilename());


        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = dbHelper.getWritableDatabase().insert(TABLE_NAME, null, values);
        return newRowId;
    }

    public ArrayList<Angle> getAnglesForSession(long sessionId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME_SESSION_ID + " =?", new String[]{String.valueOf(sessionId)}, null, null, null, null);
        return anglesFromCursor(cursor);
    }

    private ArrayList<Angle> anglesFromCursor(Cursor cursor) {
        ArrayList<Angle> angles = new ArrayList<Angle>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i ++) {
                long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
                long sessionId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_SESSION_ID));
                String notes = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NOTES));
                String filename = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FILENAME));
                angles.add(new Angle(id, sessionId, filename, notes));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return angles;
    }

    public void deleteAngle(long id) {
        String selection = COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        dbHelper.getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Update angle
     * @param angle Angle to update
     * @param values Values to update. E.g.
     *               ContentValues values = new ContentValues();
     *               values.put(COLUMN_NAME_NOTES, "New note");
     */
    public void updateAngle(Angle angle, ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(angle.getId()) };
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }
}
