package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;


public class StudentFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private Button btnNotifications;
    private ParseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student, container, false);
        activity = getActivity();
        activity.setTitle("Student");
        user = ParseUser.getCurrentUser();
        btnNotifications = (Button) rootView.findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnNotifications)) {
            NotificationFragment notificationFragment = new NotificationFragment();
            Bundle data = new Bundle();
            data.putString("NOTIFICATION_FOR", user.getString("ENROLLMENT"));
            notificationFragment.setArguments(data);
            Functions.setFragment(null, notificationFragment, "NOTIFICATION_FRAGMENT", R.id.fragmentContainerUser, true);
        }
    }
}
