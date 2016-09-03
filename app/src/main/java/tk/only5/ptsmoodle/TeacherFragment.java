package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.parse.ParseUser;

import java.io.File;

public class TeacherFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private Button btnSendNotification, btnUploadNotes;
    private ProgressDialog dialog;
    private ParseUser user;

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
          /*  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/*");
            intent=Intent.createChooser(intent,"Choose a File!");
            startActivityForResult(intent,Functions.FILE_PICK_REQUEST_CODE);*/
            DialogProperties properties = new DialogProperties();
            properties.selection_mode = DialogConfigs.SINGLE_MODE;
            properties.selection_type = DialogConfigs.FILE_SELECT;
            properties.root = new File(DialogConfigs.DEFAULT_DIR);
            properties.extensions = null;
            FilePickerDialog dialog = new FilePickerDialog(activity, properties);
            dialog.setDialogSelectionListener(new DialogSelectionListener() {
                @Override
                public void onSelectedFilePaths(final String[] files) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Log.d(TAG, "UPLOADING FILE:" + files[0]);
                                Functions.uploadFile(files[0], activity, user);
                            } catch (Exception e) {
                                Log.e(TAG, "Error", e);
                            }
                        }
                    }.start();
                }
            });
            dialog.show();
        }
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == Functions.FILE_PICK_REQUEST_CODE) {
            final ProgressDialog dialog = Functions.showLoading(activity, "Uploading File!");
            final ParseUser user = ParseUser.getCurrentUser();
            new Thread() {
                @Override
                public void run() {
                    try {
                        String path = data.getData().getPath();
                        //String path="/data/data/tk.only5.quizapp/Questions.xls";
                        Log.d(TAG, "LOCATION:" + path);
                        Functions.uploadFile(path, activity, user,dialog);
                    } catch (Exception e) {
                        Log.e(TAG, "Error", e);
                    }
                }
            }.start();
        }
    }*/
}
