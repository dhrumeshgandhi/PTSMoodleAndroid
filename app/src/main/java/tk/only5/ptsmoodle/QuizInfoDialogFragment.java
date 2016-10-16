package tk.only5.ptsmoodle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by DHRUMESH on 9/25/2016.
 */

public class QuizInfoDialogFragment extends DialogFragment implements View.OnClickListener {
    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private ParseUser user;
    private Quiz quizInfo;
    private TextView tvTitle, tvSubject, tvTeacherName, tvNoOfQue, tvPositiveMarks, tvNegativeMarks, tvTimeLimit;
    private FloatingActionButton fabGiveQuiz;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_quiz_info, container, false);
        activity = getActivity();
        getDialog().setTitle("QUIZ");
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        user = ParseUser.getCurrentUser();
        quizInfo = (Quiz) getArguments().getSerializable("QUIZ");
        tvTitle = (TextView) rootView.findViewById(R.id.tvQuizName);
        tvSubject = (TextView) rootView.findViewById(R.id.tvSubject);
        tvTeacherName = (TextView) rootView.findViewById(R.id.tvTeacher);
        tvNoOfQue = (TextView) rootView.findViewById(R.id.tvNoOfQues);
        tvTimeLimit = (TextView) rootView.findViewById(R.id.tvTimeLimit);
        tvPositiveMarks = (TextView) rootView.findViewById(R.id.tvPositiveMarks);
        tvNegativeMarks = (TextView) rootView.findViewById(R.id.tvNegativeMarks);
        tvTitle.setText(quizInfo.getName());
        tvSubject.setText(quizInfo.getSubject());
        tvTeacherName.setText(quizInfo.getTeacher_name());
        tvNoOfQue.setText(quizInfo.getNo_of_ques());
        tvTimeLimit.setText(quizInfo.getTime_limit());
        tvPositiveMarks.setText(quizInfo.getPositive_marks());
        tvNegativeMarks.setText(quizInfo.getNegative_marks());
        fabGiveQuiz = (FloatingActionButton) rootView.findViewById(R.id.fabGiveQuiz);
        fabGiveQuiz.setOnClickListener(this);
        Log.d(TAG, quizInfo.getQuestions().toString());
        return rootView;
    }

    @Override
    public void onClick(View view) {
        GivingQuizFragment givingQuizFragment = new GivingQuizFragment();
        givingQuizFragment.setArguments(getArguments());
        Functions.setFragment(null, givingQuizFragment, "GIVING_QUIZ_FRAGMENT", R.id.fragmentContainerUser, false);
        dismiss();
    }
}
