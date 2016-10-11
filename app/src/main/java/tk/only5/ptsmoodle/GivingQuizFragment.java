package tk.only5.ptsmoodle;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;


public class GivingQuizFragment extends Fragment implements View.OnClickListener, GivingQuizListAdapter.OnQuestionAnsweredListener {

    protected static String jsonAnswer;
    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private FloatingActionButton fabNext;
    private ListView lvQues;
    private GivingQuizListAdapter quesAdapter;
    private ArrayList<Question> quesList;
    private int id = 0, count = 0;
    private ArrayList<Answer> answers;
    private Quiz quiz;
    private TextView tvAnswered, tvTotal, tvTimeLeft;
    private Timer timer;
    private int correct = 0, wrong = 0;
    private double marks = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_giving_quiz, container, false);
        activity = getActivity();
        activity.setTitle("GIVING QUIZ");
        quiz = (Quiz) getArguments().getSerializable("QUIZ");
        quesList = Functions.jsonQuestionsToRandomizedQuesList(quiz.getQuestions());
        fabNext = (FloatingActionButton) rootView.findViewById(R.id.fabSubmit);
        fabNext.setOnClickListener(this);
        tvAnswered = (TextView) rootView.findViewById(R.id.tvAnswered);
        tvAnswered.setText(count + "");
        tvTotal = (TextView) rootView.findViewById(R.id.tvTotal);
        tvTotal.setText(quiz.getNo_of_ques());
        tvTimeLeft = (TextView) rootView.findViewById(R.id.tvTimeLeft);
        tvTimeLeft.setText(quiz.getTime_limit());
        lvQues = (ListView) rootView.findViewById(R.id.lvQues);
        quesAdapter = new GivingQuizListAdapter(activity, quesList, id, this);
        lvQues.setAdapter(quesAdapter);
        quesAdapter.notifyDataSetChanged();
        Log.d(TAG, "TimeLimit:" + quiz.getTime_limit() + " ==" + Integer.parseInt(quiz.getTime_limit()) * 60000);
        timer = new Timer(Integer.parseInt(quiz.getTime_limit()) * 60000);
        timer.execute();
        return rootView;
    }

    @Override
    public void onStop() {
        timer.cancel(true);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        timer.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onQuestionAnswered() {
        tvAnswered.setText((++count) + "");
    }

    @Override
    public void onClick(View v) {
        if (v.equals(fabNext)) {
            answers = quesAdapter.getAnswers();
            timer.cancel(true);
            Functions.checkQuizAndUploadResult(Functions.jsonQuestionsToQuesList(quiz.getQuestions()), answers, quiz.getPositive_marks(), quiz.getNegative_marks(), quiz, ParseUser.getCurrentUser(), timer.millis);
            //jsonAnswer = Functions.getAnswersInJson(answers).toString();
        }
    }

    class Timer extends AsyncTask {
        protected long millis;
        protected int sec = 0, min = 0;

        Timer(long millis) {
            this.millis = millis;
            tvTimeLeft.setText(quiz.getTime_limit());
        }

        @Override
        protected Object doInBackground(Object[] params) {

            FragmentManager fragmentManager = getFragmentManager();
            try {
                Thread.sleep(1000);
                while (millis > 0) {
                    millis -= 1000;
                    if (!fragmentManager.findFragmentByTag("GIVING_QUIZ_FRAGMENT").isVisible()) {
                        this.cancel(true);
                        break;
                    }
                    if (isCancelled()) break;
                    publishProgress(millis);
                    Log.d(TAG, "Millis:" + millis);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error", e);
            }
            return 0;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            if (!this.isCancelled() && getFragmentManager().findFragmentByTag("GIVING_QUIZ_FRAGMENT").isVisible()) {
                Log.d(TAG, Functions.getTimeFromMillis(millis));
                tvTimeLeft.setText(Functions.getTimeFromMillis(millis));
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            tvTimeLeft.setText("TimeOver");
            if (!this.isCancelled() && getFragmentManager().findFragmentByTag("GIVING_QUIZ_FRAGMENT").isVisible()) {
                new AlertDialog.Builder(activity)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fabNext.callOnClick();
                                dialog.dismiss();

                            }
                        })
                        .setMessage("Time's UP!!!")
                        .create()
                        .show();
            }
        }
    }
}
