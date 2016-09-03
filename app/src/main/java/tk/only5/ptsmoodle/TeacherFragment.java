package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.parse.ParseUser;

import java.io.File;

public class TeacherFragment extends Fragment implements View.OnClickListener {

    private static String topic = "";
    private static String TAG = InitClass.TAG;
    private static ProgressDialog dialog;
    private static ParseUser user;
    private View rootView;
    private Activity activity;
    private Button btnSendNotification, btnUploadNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_teacher, container, false);
        activity = getActivity();
        activity.setTitle("Teacher");
        btnSendNotification = (Button) rootView.findViewById(R.id.btnSendNotification);
        btnUploadNotes = (Button) rootView.findViewById(R.id.btnUploadNotes);
        btnSendNotification.setOnClickListener(this);
        btnUploadNotes.setOnClickListener(this);
        user = ParseUser.getCurrentUser();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSendNotification)) {
            // Functions.sendNotification("Test", "Hello", Arrays.asList("13012011012", "TEST"));
            NotificationUserSelectionDialogFragment selectionDialogFragement = new NotificationUserSelectionDialogFragment();
            selectionDialogFragement.setCancelable(false);
            selectionDialogFragement.show(getFragmentManager(), "SELECTION_DIALOG");
        } else if (view.equals(btnUploadNotes)) {
            topic = "";
            new GetTopicDialogFragment().show(getFragmentManager(), "TOPIC_DIALOG");

        }
    }

    public static class GetTopicDialogFragment extends DialogFragment {
        private View rootView;
        private Activity activity;
        private EditText etTopic;
        private Button btnNext;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.dialog_file_upload_get_topic, container, false);
            activity = getActivity();
            getDialog().setTitle("Topic");
            etTopic = (EditText) rootView.findViewById(R.id.etTopic);
            btnNext = (Button) rootView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    topic = etTopic.getText().toString();
                    if (topic.isEmpty()) topic = "";
                    if (!topic.isEmpty()) {
                        dialog = new ProgressDialog(activity);
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setTitle("Uploading File!");
                        dialog.setIndeterminate(false);
                        dialog.setCancelable(false);
                        dialog.setProgress(0);
                        dialog.setMax(100);
                        DialogProperties properties = new DialogProperties();
                        properties.selection_mode = DialogConfigs.SINGLE_MODE;
                        properties.selection_type = DialogConfigs.FILE_SELECT;
                        properties.root = new File(DialogConfigs.DEFAULT_DIR);
                        properties.extensions = null;
                        FilePickerDialog filePickerDialog = new FilePickerDialog(activity, properties);
                        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
                            @Override
                            public void onSelectedFilePaths(final String[] files) {
                                dialog.show();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Log.d(TAG, "UPLOADING FILE:" + files[0]);
                                            Functions.uploadFile(files[0], topic, activity, user.getString("TEACHER_ID"), dialog);
                                        } catch (Exception e) {
                                            Log.e(TAG, "Error", e);
                                        }
                                    }
                                }.start();
                            }
                        });
                        filePickerDialog.show();
                    }
                    dismiss();
                }
            });
            return rootView;
        }
    }
}
