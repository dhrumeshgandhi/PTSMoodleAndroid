package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private SwipeRefreshLayout srlNotification;
    private ListView lvNotification;
    private NotificationListAdapter notificationListAdapter;
    private ArrayList<Notification> notificationList;
    private String notificationFor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        activity = getActivity();
        activity.setTitle("NOTIFICATION");
        notificationFor = getArguments().getString("NOTIFICATION_FOR");
        lvNotification = (ListView) rootView.findViewById(R.id.lvNotification);
        notificationList = new ArrayList<>();
        notificationListAdapter = new NotificationListAdapter(getContext(), notificationList);
        lvNotification.setAdapter(notificationListAdapter);
        notificationListAdapter.notifyDataSetChanged();
        srlNotification = (SwipeRefreshLayout) rootView.findViewById(R.id.srlNotification);
        srlNotification.setOnRefreshListener(this);
        Functions.loadNotifications(notificationFor, activity, notificationList,
                notificationListAdapter, srlNotification);
        return rootView;
    }

    @Override
    public void onRefresh() {
        Functions.loadNotifications(notificationFor, activity, notificationList,
                notificationListAdapter, srlNotification);
    }
}
