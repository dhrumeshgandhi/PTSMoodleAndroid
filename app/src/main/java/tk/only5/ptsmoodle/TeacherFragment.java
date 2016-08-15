package tk.only5.ptsmoodle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TeacherFragment extends Fragment {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_teacher, container, false);
        activity = getActivity();
        activity.setTitle("Teacher");
        return rootView;
    }
}
