package oshannon.personaltrainer.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import oshannon.personaltrainer.AppState;

/**
 * Created by orrieshannon on 2014-09-22.
 */
public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_SESSIONS_TABLE =
            "CREATE TABLE " + SessionDbHelper.TABLE_NAME+ " (" +
                    SessionDbHelper.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    SessionDbHelper.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    SessionDbHelper.COLUMN_NAME_NOTES + TEXT_TYPE + COMMA_SEP +
                    SessionDbHelper.COLUMN_NAME_STATUS + INTEGER_TYPE +
                    " )";

    private static final String SQL_CREATE_ANGLES_TABLE =
            "CREATE TABLE " + AngleDbHelper.TABLE_NAME+ " (" +
                    AngleDbHelper.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    AngleDbHelper.COLUMN_NAME_SESSION_ID + INTEGER_TYPE + COMMA_SEP +
                    AngleDbHelper.COLUMN_NAME_FILENAME + TEXT_TYPE + COMMA_SEP +
                    AngleDbHelper.COLUMN_NAME_NOTES + TEXT_TYPE +
                    " )";

    private static DbHelper instance;

    /**
     * Constructor for DbHelper
     */
    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper(AppState.getInstance());
        }
        return instance;
    }

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + SessionDbHelper.TABLE_NAME;

    private DbHelper() {
        super(AppState.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SESSIONS_TABLE);
        db.execSQL(SQL_CREATE_ANGLES_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // TODO: Migration
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /** Output Content for All Categories in DB */
    public void printContents(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(true, tableName, null, null, null, null, null, null, null);
        c.moveToFirst();
        // Print the column headers
        String headers = "";
        for (int j = 0; j < c.getColumnCount(); j++) {
            headers += c.getColumnName(j) + "|";
        }
        System.out.println(headers);
        System.out.println("------------------------------------------------");
        String rowString = "";
        for (int i = 0; i < c.getCount(); i++) {
            for (int j = 0; j < c.getColumnCount(); j++) {
                rowString += c.getString(j) + " | ";
            }
            System.out.println(rowString + " | ");
            c.moveToNext();
            rowString = "";
        }
        System.out.println("");
    }


}
