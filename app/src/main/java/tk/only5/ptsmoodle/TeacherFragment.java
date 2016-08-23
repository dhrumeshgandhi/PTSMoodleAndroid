package tk.only5.ptsmoodle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TeacherFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private Button btnSendNotification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_teacher, container, false);
        activity = getActivity();
        activity.setTitle("Teacher");
        btnSendNotification = (Button) rootView.findViewById(R.id.btnSendNotification);
        btnSendNotification.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSendNotification)) {
            // Functions.sendNotification("Test", "Hello", Arrays.asList("13012011012", "TEST"));
            NotificationUserSelectionDialogFragment selectionDialogFragement = new NotificationUserSelectionDialogFragment();
            selectionDialogFragement.setCancelable(false);
            selectionDialogFragement.show(getFragmentManager(), "SELECTION_DIALOG");
        }
    }
}
