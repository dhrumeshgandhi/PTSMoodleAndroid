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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.parse.ParseUser;

import java.io.File;

public class TeacherFragment extends Fragment implements View.OnClickListener {

    private static String subject = "", branch = "", sem = "";
    private static String TAG = InitClass.TAG;
    private static ProgressDialog dialog;
    private static ParseUser user;
    private View rootView;
    private Activity activity;
    private Button btnSendNotification, btnUploadNotes, btnAddQuiz;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_teacher, container, false);
        activity = getActivity();
        activity.setTitle("Teacher");
        btnSendNotification = (Button) rootView.findViewById(R.id.btnSendNotification);
        btnUploadNotes = (Button) rootView.findViewById(R.id.btnUploadNotes);
        btnAddQuiz = (Button) rootView.findViewById(R.id.btnAddQuiz);
        btnSendNotification.setOnClickListener(this);
        btnUploadNotes.setOnClickListener(this);
        btnAddQuiz.setOnClickListener(this);
        user = ParseUser.getCurrentUser();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSendNotification)) {
            NotificationUserSelectionDialogFragment selectionDialogFragement = new NotificationUserSelectionDialogFragment();
            selectionDialogFragement.setCancelable(false);
            selectionDialogFragement.show(getFragmentManager(), "SELECTION_DIALOG");
        } else if (view.equals(btnUploadNotes)) {
            subject = "";
            new GetTopicDialogFragment().show(getFragmentManager(), "TOPIC_DIALOG");
        } else if (view.equals(btnAddQuiz)) {

        }
    }

    public static class GetTopicDialogFragment extends DialogFragment {
        private View rootView;
        private Activity activity;
        private EditText etSubject;
        private Spinner spinSem, spinBranch;
        private Button btnNext;
        private ArrayAdapter<String> semAdapter, branchAdapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.dialog_file_upload_info, container, false);
            activity = getActivity();
            getDialog().setTitle("Document Info");
            etSubject = (EditText) rootView.findViewById(R.id.etSubejct);
            spinBranch = (Spinner) rootView.findViewById(R.id.spinBranch);
            spinSem = (Spinner) rootView.findViewById(R.id.spinSem);
            semAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SEM_TEACHER);
            spinSem.setAdapter(semAdapter);
            semAdapter.notifyDataSetChanged();
            branchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.BRANCH_TEACHER);
            spinBranch.setAdapter(branchAdapter);
            branchAdapter.notifyDataSetChanged();
            btnNext = (Button) rootView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subject = etSubject.getText().toString();
                    branch = spinBranch.getSelectedItem().toString();
                    sem = spinSem.getSelectedItem().toString();
                    if (!subject.isEmpty()) {
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
                                            Functions.uploadFile(files[0], subject, sem, branch, activity, user, dialog);
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
