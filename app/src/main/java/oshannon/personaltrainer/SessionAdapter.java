package oshannon.personaltrainer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import oshannon.personaltrainer.models.Session;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by orrieshannon on 2014-09-28.
 */
public class SessionAdapter extends BaseAdapter {

    private List<Session> sessions = Collections.emptyList();

    private final Context context;

    // the context is needed to inflate views in getView()
    public SessionAdapter(Context context) {
        this.context = context;
    }

    public void updateSessions(List<Session> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    public void addSession(Session session) {
        this.sessions.add(session);
        notifyDataSetChanged();
    }

    public void removeSession(Session sessionToRemove) {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (sessionToRemove.getId() == session.getId()) {
                    sessions.remove(session);
                    notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to Session thanks to Java return type covariance
    @Override
    public Session getItem(int position) {
        return sessions.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView sessionPhoto;
        TextView dateView;
        TextView statusView;
        TextView descriptionView;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.session_list_item, parent, false);
            sessionPhoto = (ImageView) convertView.findViewById(R.id.session_photo);
            dateView = (TextView) convertView.findViewById(R.id.date);
            statusView = (TextView) convertView.findViewById(R.id.status);
            descriptionView = (TextView) convertView.findViewById(R.id.description);

            convertView.setTag(new ViewHolder(sessionPhoto, dateView, statusView, descriptionView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            sessionPhoto = viewHolder.sessionPhoto;
            dateView = viewHolder.dateView;
            statusView = viewHolder.statusView;
            descriptionView = viewHolder.descriptionView;
        }

        Session session = getItem(position);
        dateView.setText(session.getDate().toString());
        sessionPhoto.setImageBitmap(session.getAngles().get(0).getThumbnail());

        return convertView;
    }

    private static class ViewHolder {
        public final ImageView sessionPhoto;
        public final TextView dateView;
        public final TextView statusView;
        public final TextView descriptionView;


        public ViewHolder(ImageView sessionPhoto, TextView dateView, TextView statusView, TextView descriptionView) {
            this.sessionPhoto = sessionPhoto;
            this.statusView = statusView;
            this.dateView = dateView;
            this.descriptionView = descriptionView;
        }
    }

}
