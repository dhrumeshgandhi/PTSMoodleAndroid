package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;


public class ParentFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private Button btnNotifications, btnCheckAttendance;
    private TextView tvStudentName, tvStudentEnroll;
    private ParseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_parent, container, false);
        activity = getActivity();
        activity.setTitle("Parent");
        user = ParseUser.getCurrentUser();
        btnNotifications = (Button) rootView.findViewById(R.id.btnNotifications);
        btnCheckAttendance = (Button) rootView.findViewById(R.id.btnCheckAttendance);
        tvStudentName = (TextView) rootView.findViewById(R.id.tvStudentName);
        tvStudentEnroll = (TextView) rootView.findViewById(R.id.tvStudentEnroll);
        tvStudentName.setText(user.getString("STUDENT_NAME"));
        tvStudentEnroll.setText(user.getString("STUDENT_ENROLL"));
        btnNotifications.setOnClickListener(this);
        btnCheckAttendance.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnNotifications)) {
            NotificationFragment notificationFragment = new NotificationFragment();
            Bundle data = new Bundle();
            data.putString("NOTIFICATION_FOR", "P" + user.getString("STUDENT_ENROLL"));
            notificationFragment.setArguments(data);
            Functions.setFragment(null, notificationFragment, "NOTIFICATION_FRAGMENT", R.id.fragmentContainerUser, true);
        } else if (view.equals(btnCheckAttendance)) {
            Functions.setFragment(null, new AttendanceFragment(), "ATTENDANCE_FRAGEMENT", R.id.fragmentContainerUser, true);
        }
    }
}
