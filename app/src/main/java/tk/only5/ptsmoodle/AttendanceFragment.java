package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;
//ATTENDANCE LIST ADAPTER AND ROW

public class AttendanceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private Activity activity;
    private ParseUser user, student;
    private String TAG = InitClass.TAG;
    private SwipeRefreshLayout srlAttendance;
    private ListView lvAttendance;
    private AttendanceListAdapter attendanceListAdapter;
    private ArrayList<Attendance> attendanceList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_attendance, container, false);
        activity = getActivity();
        activity.setTitle("ATTENDANCE");
        user = ParseUser.getCurrentUser();
        lvAttendance = (ListView) rootView.findViewById(R.id.lvAttendance);
        attendanceList = new ArrayList<>();
        attendanceListAdapter = new AttendanceListAdapter(getContext(), attendanceList);
        lvAttendance.setAdapter(attendanceListAdapter);
        attendanceListAdapter.notifyDataSetChanged();
        srlAttendance = (SwipeRefreshLayout) rootView.findViewById(R.id.srlAttendance);
        srlAttendance.setColorSchemeResources(R.color.swipe_refresh_color);
        srlAttendance.setOnRefreshListener(this);
        try {
            if (user.getString("POST").equals("Parent")) {
                student = user.
                        getParseUser("PARENT_OF")
                        .fetchIfNeeded();
                Functions.loadAttendance(student, activity, attendanceList,
                        attendanceListAdapter, srlAttendance);

            } else {
                Functions.loadAttendance(user, activity, attendanceList,
                        attendanceListAdapter, srlAttendance);
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }
        return rootView;
    }

    @Override
    public void onRefresh() {
        try {
            if (user.getString("POST").equals("Parent")) {
                Functions.loadAttendance(student, activity, attendanceList,
                        attendanceListAdapter, srlAttendance);
            } else {
                Functions.loadAttendance(user, activity, attendanceList,
                        attendanceListAdapter, srlAttendance);
            }
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }
    }

}
