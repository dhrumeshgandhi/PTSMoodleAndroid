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
    private Button btnNotifications, btnNotes, btnGiveQuiz;
    private ParseUser user;
    private Bundle data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_student, container, false);
        activity = getActivity();
        activity.setTitle("Student");
        user = ParseUser.getCurrentUser();
        btnNotifications = (Button) rootView.findViewById(R.id.btnNotifications);
        btnNotes = (Button) rootView.findViewById(R.id.btnNotes);
        btnGiveQuiz = (Button) rootView.findViewById(R.id.btnGiveQuiz);
        btnNotifications.setOnClickListener(this);
        btnNotes.setOnClickListener(this);
        btnGiveQuiz.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnNotifications)) {
            NotificationFragment notificationFragment = new NotificationFragment();
            data = new Bundle();
            data.putString("NOTIFICATION_FOR", user.getString("ENROLLMENT"));
            notificationFragment.setArguments(data);
            Functions.setFragment(null, notificationFragment, "NOTIFICATION_FRAGMENT", R.id.fragmentContainerUser, true);
        } else if (view.equals(btnNotes)) {
            NotesFragment notesFragment = new NotesFragment();
            data = new Bundle();
            data.putString("SEM", user.getString("SEMESTER"));
            data.putString("BRANCH", user.getString("BRANCH"));
            notesFragment.setArguments(data);
            Functions.setFragment(null, notesFragment, "NOTES_FRAGMENT", R.id.fragmentContainerUser, true);
        } else if (view.equals(btnGiveQuiz)) {
            
        }
    }
}
