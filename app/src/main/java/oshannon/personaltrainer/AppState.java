package oshannon.personaltrainer;

import android.app.Application;

/**
 * Created by orrieshannon on 2014-09-22.
 */
public class AppState extends Application {
    private static Application instance;
    public static final String APPLICATION_NAME = "probile";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
