package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class NotificationUserSelectionDialogViewsFragement extends Fragment {

    private static Activity activity;
    private static EditText etTitle, etMessage;
    private static Spinner spinSem, spinBranch;
    private static String title = "", message = "", sem = "", branch = "";
    private static CheckBox ctvSelectAll;
    private static ArrayAdapter<String> semAdapter, branchAdapter, studentAdapter;
    private static ArrayList<String> studentList = new ArrayList<>();
    private static NonScrollListView lvStudentList;
    private static List<String> selectedStudentsList = new ArrayList<>();
    private static String selectedStudent = "";
    private static ParseUser user = ParseUser.getCurrentUser();
    private static TextView tvStudentList;
    int pos;
    private View rootView;
    private String TAG = InitClass.TAG;

    protected static void getNotificationInfo() {
        title = etTitle.getText().toString();
        message = etMessage.getText().toString();
        sem = spinSem.getSelectedItem().toString();
        branch = spinBranch.getSelectedItem().toString();
        if (title.isEmpty() || message.isEmpty() || sem.isEmpty() || branch.isEmpty()) {
            Toast.makeText(activity, "Please Fill All Details", Toast.LENGTH_LONG).show();
        } else {
            if (!sem.isEmpty() && !branch.isEmpty()) {
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("SEMESTER", sem);
                query.whereEqualTo("BRANCH", branch);
                query.addAscendingOrder("ENROLLMENT");
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        studentList.clear();
                        for (ParseUser user : objects) {
                            studentList.add(user.getString("ENROLLMENT"));
                        }
                        studentAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    protected static void sendNotification() {
        if (lvStudentList.getCount() > 0) {
            SparseBooleanArray checkedList = lvStudentList.getCheckedItemPositions();
            for (int i = 0; i < lvStudentList.getCount(); i++) {
                if (checkedList.get(i)) {
                    selectedStudent += lvStudentList.getItemAtPosition(i) + "\n";
                    selectedStudentsList.add((String) lvStudentList.getItemAtPosition(i));
                }
            }
            Functions.sendNotification(title, message, selectedStudentsList);
            Functions.addNotificationToList(title, message, selectedStudentsList, user.getString("TEACHER_ID"));
            tvStudentList.setText(selectedStudent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pos = getArguments().getInt("POS");
        activity = getActivity();
        Log.d(TAG, "Current Viewpager:" + pos);
        switch (pos) {
            case 0:
                rootView = inflater.inflate(
                        R.layout.fragment_notification_user_selection_dialog_view1_fragement,
                        container, false);
                etTitle = (EditText) rootView.findViewById(R.id.etNotificationTitle);
                etMessage = (EditText) rootView.findViewById(R.id.etNotificationMessage);
                spinBranch = (Spinner) rootView.findViewById(R.id.spinBranch);
                spinSem = (Spinner) rootView.findViewById(R.id.spinSem);
                semAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SEM_TEACHER);
                spinSem.setAdapter(semAdapter);
                semAdapter.notifyDataSetChanged();
                branchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.BRANCH_TEACHER);
                spinBranch.setAdapter(branchAdapter);
                branchAdapter.notifyDataSetChanged();
                break;
            case 1:
                rootView = inflater.inflate(
                        R.layout.fragment_notification_user_selection_dialog_view2_fragement,
                        container, false);
                lvStudentList = (NonScrollListView) rootView.findViewById(R.id.lvNotificationStudentList);
                lvStudentList.setChoiceMode(NonScrollListView.CHOICE_MODE_MULTIPLE);
                studentAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_multiple_choice, studentList);
                lvStudentList.setAdapter(studentAdapter);
                ctvSelectAll = (CheckBox) rootView.findViewById(R.id.cbSelectAll);
                ctvSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            for (int i = 0; i < lvStudentList.getCount(); i++) {
                                lvStudentList.setItemChecked(i, true);
                            }
                        } else {
                            for (int i = 0; i < lvStudentList.getCount(); i++) {
                                lvStudentList.setItemChecked(i, false);
                            }
                        }
                    }
                });
                break;
            case 2:
                rootView = inflater.inflate(
                        R.layout.fragment_notification_user_selection_dialog_view3_fragement,
                        container, false);
                tvStudentList = (TextView) rootView.findViewById(R.id.tvStudentList);
                tvStudentList.setText(selectedStudent);
                break;
        }
        return rootView;
    }
}
