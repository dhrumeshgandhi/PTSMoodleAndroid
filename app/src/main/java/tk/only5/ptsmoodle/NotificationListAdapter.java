package tk.only5.ptsmoodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DHRUMESH on 8/2/2016.
 */
public class NotificationListAdapter extends BaseAdapter {
    ViewHolder holder = null;
    private LayoutInflater li;
    private ArrayList<Notification> notificationList;

    public NotificationListAdapter(Context context, ArrayList<Notification> notificationList) {
        this.li = LayoutInflater.from(context);
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Notification getItem(int position) {
        return notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = li.inflate(R.layout.list_row_notification, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvNotificationStudentSelectionEnroll);
            holder.tvDateTime = (TextView) convertView.findViewById(R.id.tvNotificationDateTime);
            holder.tvMessage = (TextView) convertView.findViewById(R.id.tvNotificationMessage);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tvTitle.setText(notificationList.get(position).getTitle());
        holder.tvDateTime.setText(notificationList.get(position).getDateTime());
        holder.tvMessage.setText(notificationList.get(position).getMessage());
        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle, tvDateTime, tvMessage;
    }

}