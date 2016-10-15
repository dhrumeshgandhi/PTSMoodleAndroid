package tk.only5.ptsmoodle;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class AddAttendanceDialogViewsFragment extends DialogFragment {
    private static Activity activity;
    private static DatePickerEditText dpetDate;
    private static Spinner spinSem, spinBranch, spinSubject;
    private static String date, sem = "", branch = "";
    private static CheckBox ctvSelectAll;
    private static ArrayAdapter<String> semAdapter, branchAdapter, subjectAdapter, studentAdapter;
    private static ArrayList<String> studentList = new ArrayList<>();
    private static NonScrollListView lvStudentList;
    private static List<String> selectedStudentsList = new ArrayList<>();
    private static String selectedStudent = "";
    private static ParseUser user = ParseUser.getCurrentUser();
    private static TextView tvStudentList;
    int pos;
    private View rootView;
    private String TAG = InitClass.TAG;

    protected static void getAttendanceInfo() {
        date = dpetDate.getText().toString();
        sem = spinSem.getSelectedItem().toString();
        branch = spinBranch.getSelectedItem().toString();
        if (date.isEmpty() || sem.isEmpty() || branch.isEmpty()) {
            Toast.makeText(activity, "Please Fill All Details", Toast.LENGTH_LONG).show();
        } else {
            if (!sem.isEmpty() && !branch.isEmpty()) {
                final ProgressDialog dialog = Functions.showLoading(activity, "Loading Students!");
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
                        Toast.makeText(activity, "No Student Found!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    protected static void addAttendance() {
        if (lvStudentList.getCount() > 0) {
            SparseBooleanArray checkedList = lvStudentList.getCheckedItemPositions();
            for (int i = 0; i < lvStudentList.getCount(); i++) {
                if (checkedList.get(i)) {
                    selectedStudent += lvStudentList.getItemAtPosition(i) + "\n";
                    selectedStudentsList.add((String) lvStudentList.getItemAtPosition(i));
                }
            }
            if (selectedStudentsList.size() > 0) {
                tvStudentList.setText(selectedStudent + " Date:" + date);
            } else {
                tvStudentList.setText("None");
            }
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
                        R.layout.dialog_add_attendence_view1,
                        container, false);
                dpetDate = (DatePickerEditText) rootView.findViewById(R.id.dpetDate);
                spinBranch = (Spinner) rootView.findViewById(R.id.spinBranch);
                spinSem = (Spinner) rootView.findViewById(R.id.spinSem);
                spinSubject = (Spinner) rootView.findViewById(R.id.spinSubject);
                semAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SEM_TEACHER);
                spinSem.setAdapter(semAdapter);
                semAdapter.notifyDataSetChanged();
                branchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.BRANCH_TEACHER);
                spinBranch.setAdapter(branchAdapter);
                branchAdapter.notifyDataSetChanged();
                subjectAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SUBJECT_TEACHER);
                spinSubject.setAdapter(subjectAdapter);
                subjectAdapter.notifyDataSetChanged();
                break;
            case 1:
                rootView = inflater.inflate(
                        R.layout.dialog_add_attendence_view2,
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
                        R.layout.dialog_add_attendence_view3,
                        container, false);
                tvStudentList = (TextView) rootView.findViewById(R.id.tvStudentList);
                tvStudentList.setText(selectedStudent);
                break;
        }
        return rootView;
    }
}
