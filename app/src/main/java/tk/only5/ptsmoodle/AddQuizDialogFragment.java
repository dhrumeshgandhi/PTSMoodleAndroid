package tk.only5.ptsmoodle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseUser;

/**
 * Created by DHRUMESH on 9/25/2016.
 */

public class AddQuizDialogFragment extends DialogFragment implements View.OnClickListener {
    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private ParseUser user;
    private EditText etTitle, etSubject, etNoOfQue, etTimeLimit, etPositiveMarks, etNegativeMarks;
    private Spinner spinSem, spinBranch;
    private ArrayAdapter<String> semAdapter, branchAdapter;
    private String title, subject, no_of_que, time_limit, positive_marks, negative_marks, branch, sem;
    private FloatingActionButton fabAddQuiz;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_add_quiz_info, container, false);
        activity = getActivity();
        getDialog().setTitle("Add Quiz");
        getDialog().setCancelable(true);
        user = ParseUser.getCurrentUser();
        etTitle = (EditText) rootView.findViewById(R.id.etQuizTitle);
        etSubject = (EditText) rootView.findViewById(R.id.etSubejct);
        etNoOfQue = (EditText) rootView.findViewById(R.id.etNoOfQue);
        etTimeLimit = (EditText) rootView.findViewById(R.id.etTimeLimit);
        etPositiveMarks = (EditText) rootView.findViewById(R.id.etPositiveMarks);
        etNegativeMarks = (EditText) rootView.findViewById(R.id.etNegativeMarks);
        fabAddQuiz = (FloatingActionButton) rootView.findViewById(R.id.fabAddQuiz);
        fabAddQuiz.setOnClickListener(this);
        spinBranch = (Spinner) rootView.findViewById(R.id.spinBranch);
        spinSem = (Spinner) rootView.findViewById(R.id.spinSem);
        semAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.SEM_TEACHER);
        spinSem.setAdapter(semAdapter);
        semAdapter.notifyDataSetChanged();
        branchAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, Functions.BRANCH_TEACHER);
        spinBranch.setAdapter(branchAdapter);
        branchAdapter.notifyDataSetChanged();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fabAddQuiz)) {
            title = etTitle.getText().toString();
            subject = etSubject.getText().toString();
            no_of_que = etNoOfQue.getText().toString();
            time_limit = etTimeLimit.getText().toString();
            positive_marks = etPositiveMarks.getText().toString();
            negative_marks = etNegativeMarks.getText().toString();
            sem = spinSem.getSelectedItem().toString();
            branch = spinBranch.getSelectedItem().toString();
            if (title.isEmpty() || subject.isEmpty() || no_of_que.isEmpty() || time_limit.isEmpty()
                    || positive_marks.isEmpty() || negative_marks.isEmpty()) {
                Log.d(TAG, "ADD QUIZ DIALOG: INSUFFICIENT INFO");
                Snackbar.make(getDialog().findViewById(R.id.rlDialog), "Please Fill all details", Snackbar.LENGTH_LONG).show();
            } else {
                OnDialogSubmitListener onDialogSubmitListener =
                        (OnDialogSubmitListener) getTargetFragment();
                onDialogSubmitListener.onDialogSubmit(new Quiz(title, subject, no_of_que,
                        positive_marks, negative_marks, time_limit, user.getString("TEACHER_ID"),
                        branch, sem, Functions.getCurrentDateTime()));
                dismiss();
            }
        }
    }

    public interface OnDialogSubmitListener {
        void onDialogSubmit(Quiz quizData);
    }
}
