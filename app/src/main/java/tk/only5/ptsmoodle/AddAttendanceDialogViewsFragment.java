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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class AddAttendanceDialogViewsFragment extends DialogFragment {
    private static Activity activity;
    private static DatePickerEditText dpetDate;
    private static Spinner spinSem, spinBranch, spinSubject;
    private static String date, sem = "", branch = "", subject = "";
    private static CheckBox ctvSelectAll;
    private static ArrayAdapter<String> semAdapter, branchAdapter, subjectAdapter, studentAdapter;
    private static ArrayList<String> studentList = new ArrayList<>();
    private static NonScrollListView lvStudentList;
    private static List<String> selectedStudentsList = new ArrayList<>();
    private static String presentStudent = "", absentStudent = "";
    private static int present = 0, absent = 0;
    private static ParseUser user = ParseUser.getCurrentUser();
    private static TextView tvPresentList, tvAbsentList, tvPresentCount, tvAbsentCount, tvTotal;
    private static String TAG = InitClass.TAG;
    int pos;
    private View rootView;

    protected static void getAttendanceInfo() {
        date = dpetDate.getText().toString();
        sem = spinSem.getSelectedItem().toString();
        branch = spinBranch.getSelectedItem().toString();
        subject = spinSubject.getSelectedItem().toString();
        if (date.isEmpty() || sem.isEmpty() || branch.isEmpty() || subject.isEmpty()) {
            Toast.makeText(activity, "Please Fill All Details", Toast.LENGTH_LONG).show();
        } else {
            if (!sem.isEmpty() && !branch.isEmpty() && !subject.isEmpty()) {
                final ProgressDialog dialog = Functions.showLoading(activity, "Loading Students!");
                ParseQuery<ParseObject> queryAttendance = ParseQuery.getQuery("Attendance");
                queryAttendance.whereEqualTo("DATE", date);
                queryAttendance.whereEqualTo("SUBJECT", subject);
                queryAttendance.whereEqualTo("BRANCH", branch);
                queryAttendance.whereEqualTo("SEMESTER", sem);
                queryAttendance.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject attendance, ParseException e) {
                        ctvSelectAll.setChecked(false);
                        studentList.clear();
                        studentAdapter.notifyDataSetChanged();
                        if (attendance != null) {
                            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
                            queryUser.whereEqualTo("SEMESTER", sem);
                            queryUser.whereEqualTo("BRANCH", branch);
                            queryUser.addAscendingOrder("ENROLLMENT");
                            queryUser.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> users, ParseException e) {
                                    JSONArray jsonPresentArray, jsonAbsentArray;
                                    jsonAbsentArray = attendance.getJSONArray("ABSENT");
                                    jsonPresentArray = attendance.getJSONArray("PRESENT");
                                    String enroll;
                                    try {
                                        int j = 0;
                                        for (ParseUser user : users) {
                                            enroll = user.getString("ENROLLMENT");
                                            studentList.add(enroll);
                                            for (int i = 0; i < jsonPresentArray.length(); i++) {
                                                if (jsonPresentArray.getString(i).equals(enroll)) {
                                                    lvStudentList.setItemChecked(j, true);
                                                }
                                            }
                                            for (int i = 0; i < jsonAbsentArray.length(); i++) {
                                                if (jsonAbsentArray.getString(i).equals(enroll)) {
                                                    lvStudentList.setItemChecked(j, false);
                                                }
                                            }
                                            j++;
                                        }
                                        studentAdapter.notifyDataSetChanged();
                                        if (studentList.size() < 1) {
                                            AddAttendanceDialogFragment.fabPrevious.callOnClick();
                                            Toast.makeText(activity, "No Student Found!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e1) {
                                        Log.e(TAG, "ERROR", e1);
                                    }
                                }
                            });
                        } else if (attendance == null && e.getCode() == 101 && e.getMessage().equals("no results found for query")) {
                            ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
                            queryUser.whereEqualTo("SEMESTER", sem);
                            queryUser.whereEqualTo("BRANCH", branch);
                            queryUser.addAscendingOrder("ENROLLMENT");
                            queryUser.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> users, ParseException e) {
                                    for (ParseUser user : users) {
                                        studentList.add(user.getString("ENROLLMENT"));
                                    }
                                    studentAdapter.notifyDataSetChanged();
                                    if (studentList.size() < 1) {
                                        AddAttendanceDialogFragment.fabPrevious.callOnClick();
                                        Toast.makeText(activity, "No Student Found!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "ERROR", e);
                        }
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    protected static void addAttendance() {
        if (lvStudentList.getCount() > 0) {
            SparseBooleanArray checkedList = lvStudentList.getCheckedItemPositions();
            presentStudent = "";
            absentStudent = "";
            present = 0;
            absent = 0;
            selectedStudentsList.clear();
            for (int i = 0; i < lvStudentList.getCount(); i++) {
                if (checkedList.get(i)) {
                    presentStudent += lvStudentList.getItemAtPosition(i) + "  ";
                    present++;
                    if (present % 2 == 0) presentStudent += "\n";
                    selectedStudentsList.add((String) lvStudentList.getItemAtPosition(i));
                } else {
                    absentStudent += lvStudentList.getItemAtPosition(i) + "  ";
                    absent++;
                    if (absent % 2 == 0) absentStudent += "\n";
                    selectedStudentsList.add((String) lvStudentList.getItemAtPosition(i));
                }
            }
            Functions.uploadAttendance(user, date, subject, (present + absent) + "", present + "",
                    absent + "", branch, sem, checkedList, selectedStudentsList);
            tvTotal.setText("Total Students : " + (present + absent));
            tvAbsentCount.setText("[" + absent + "]");
            tvPresentCount.setText("[" + present + "]");
            tvPresentList.setText(presentStudent);
            tvAbsentList.setText(absentStudent);
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
                tvTotal = (TextView) rootView.findViewById(R.id.tvTotal);
                tvAbsentCount = (TextView) rootView.findViewById(R.id.tvAbsentCount);
                tvPresentCount = (TextView) rootView.findViewById(R.id.tvPresentCount);
                tvPresentList = (TextView) rootView.findViewById(R.id.tvPresentList);
                tvAbsentList = (TextView) rootView.findViewById(R.id.tvAbesentList);
                break;
        }
        return rootView;
    }
}
