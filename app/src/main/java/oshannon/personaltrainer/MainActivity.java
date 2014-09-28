package oshannon.personaltrainer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import oshannon.personaltrainer.models.Angle;
import oshannon.personaltrainer.models.Session;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_NEW_SESSION = 200;

    private ListView sessionListView;
    private SessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionListView = (ListView) findViewById(R.id.session_list);

        ArrayList<Session> sessions = SessionManager.getInstance().getSessions();
        adapter = new SessionAdapter(MainActivity.this);
        sessionListView.setAdapter(adapter);
        adapter.updateSessions(sessions);
        SessionManager.getInstance().addSessionsCallback(new SessionManager.SessionsCallback() {
            @Override
            public void sessionAdded(Session session) {
                adapter.addSession(session);
            }

            @Override
            public void sessionDeleted() {

            }

            @Override
            public void sessionUpdated() {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_capture_video) {
            newSession();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newSession() {
        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Uri fileUri = Uri.fromFile(Angle.getNewVideoFile());  // create a file to save the video
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

        // start the Video Capture Intent
        startActivityForResult(intent, REQUEST_CODE_NEW_SESSION);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_NEW_SESSION) {
            if (resultCode == RESULT_OK) {
                Angle angle = new Angle(data.getData());
                SessionManager.getInstance().createNewSession(angle);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }



}
