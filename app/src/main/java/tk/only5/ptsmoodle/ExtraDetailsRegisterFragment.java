package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;


public class ExtraDetailsRegisterFragment extends Fragment implements View.OnClickListener {

    private String post;
    private View rootView, dialogView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private Bundle data;
    private Spinner spinStudentExtraDetailSem, spinStudentExtraDetailBranch;
    private ArrayAdapter<String> spinStudentExtraDetailSemAdapter, spinStudentExtraDetailBranchAdapter;
    private FloatingActionButton fabTeacherAddClassSubject;
    private View[] views;
    private EditText etTeacherId, etStudentEnrollment, etStudentName, etDialogSubject;
    private Spinner spinDialogSem, spinDialogBranch;
    private ListView lvClassSubject;
    private ArrayList<ExtraDetailsTeacherClassSubject> listClassSubject = new ArrayList<>();
    private AddClassSubjectListAdapter lvAdapterClassSubject;
    private LayoutInflater inflater;
    private ViewGroup container;
    private AlertDialog dialogAddClassSubject;
    private Button btnDialogAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        activity = getActivity();
        this.inflater = inflater;
        this.container = container;
        data = getArguments();
        switch (post = data.getString(getResources().getString(R.string.tag_extra_details))) {
            case "Teacher":
                rootView = inflater.inflate(R.layout.fragment_extra_details_register_teacher, container, false);
                fabTeacherAddClassSubject = (FloatingActionButton) rootView.findViewById(R.id.fabTeacherExtraDetailsAddClassSubject);
                fabTeacherAddClassSubject.setOnClickListener(this);
                etTeacherId = (EditText) rootView.findViewById(R.id.etTeacherExtraDetailsId);
                lvClassSubject = (NonScrollListView) rootView.findViewById(R.id.lvTeacherExtraDetailsClassSubjects);
                lvAdapterClassSubject = new AddClassSubjectListAdapter(activity, listClassSubject);
                lvClassSubject.setAdapter(lvAdapterClassSubject);
                lvAdapterClassSubject.notifyDataSetChanged();
                views = new View[]{etTeacherId, lvClassSubject};
                Functions.setChildFragmentViews(views);
                break;
            case "Student":
                rootView = inflater.inflate(R.layout.fragment_extra_details_register_student, container, false);
                etStudentEnrollment = (EditText) rootView.findViewById(R.id.etStudentExtraDetailsEnroll);
                spinStudentExtraDetailSem = (Spinner) rootView.findViewById(R.id.spinSem);
                spinStudentExtraDetailSemAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SEMESTERS);
                spinStudentExtraDetailSem.setAdapter(spinStudentExtraDetailSemAdapter);
                spinStudentExtraDetailSemAdapter.notifyDataSetChanged();
                spinStudentExtraDetailBranch = (Spinner) rootView.findViewById(R.id.spinBranch);
                spinStudentExtraDetailBranchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.BRANCHES);
                spinStudentExtraDetailBranch.setAdapter(spinStudentExtraDetailBranchAdapter);
                spinStudentExtraDetailBranchAdapter.notifyDataSetChanged();
                views = new View[]{etStudentEnrollment, spinStudentExtraDetailSem, spinStudentExtraDetailBranch};
                Functions.setChildFragmentViews(views);
                break;
            case "Parent":
                rootView = inflater.inflate(R.layout.fragment_extra_details_register_parent, container, false);
                etStudentName = (EditText) rootView.findViewById(R.id.etParentExtraDetailsStudentName);
                etStudentEnrollment = (EditText) rootView.findViewById(R.id.etParentExtraDetailsStudentEnroll);
                views = new View[]{etStudentName, etStudentEnrollment};
                Functions.setChildFragmentViews(views);
                break;
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fabTeacherAddClassSubject)) {
            dialogView = inflater.inflate(R.layout.fragment_dialog_add_class_subject, container, false);
            dialogAddClassSubject = new AlertDialog.Builder(getContext())
                    .setView(dialogView)
                    .setCancelable(false)
                    .setTitle("Add Class and Subject")
                    .show();
            spinDialogSem = (Spinner) dialogView.findViewById(R.id.spinSem);
            spinDialogBranch = (Spinner) dialogView.findViewById(R.id.spinBranch);
            spinStudentExtraDetailSemAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SEMESTERS);
            spinDialogSem.setAdapter(spinStudentExtraDetailSemAdapter);
            spinStudentExtraDetailSemAdapter.notifyDataSetChanged();
            spinStudentExtraDetailBranchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.BRANCHES);
            spinDialogBranch.setAdapter(spinStudentExtraDetailBranchAdapter);
            spinStudentExtraDetailBranchAdapter.notifyDataSetChanged();
            etDialogSubject = (EditText) dialogView.findViewById(R.id.etDialogAddSubject);
            btnDialogAdd = (Button) dialogView.findViewById(R.id.btnDialogAdd);
            btnDialogAdd.setOnClickListener(this);
        } else if (view.equals(btnDialogAdd) && !dialogAddClassSubject.equals(null)) {
            dialogAddClassSubject.dismiss();
            String semItem, subjectItem, branchItem;
            semItem = spinDialogSem.getSelectedItem().toString();
            branchItem = spinDialogBranch.getSelectedItem().toString();
            subjectItem = etDialogSubject.getText().toString();
            if (!semItem.isEmpty() && !subjectItem.isEmpty())
                addToList(new ExtraDetailsTeacherClassSubject(semItem, branchItem, subjectItem));
        }
        //DialogAddClassSubjectFragment dialogAddClassSubjectFragment = new DialogAddClassSubjectFragment();
        //dialogAddClassSubjectFragment.show(getChildFragmentManager(), "TEST");
    }

    protected void addToList(ExtraDetailsTeacherClassSubject extraDetailsTeacherClassSubject) {
        Log.d(TAG, "ADDED");
        listClassSubject.add(extraDetailsTeacherClassSubject);
        lvAdapterClassSubject.notifyDataSetChanged();
    }
}
