package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class GiveQuizFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private SwipeRefreshLayout srlQuiz;
    private ListView lvQuiz;
    private QuizListAdapter quizListAdapter;
    private ArrayList<Quiz> quizList;
    private String sem, branch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_give_quiz, container, false);
        activity = getActivity();
        activity.setTitle("GIVE QUIZ");
        sem = getArguments().getString("SEM");
        branch = getArguments().getString("BRANCH");
        lvQuiz = (ListView) rootView.findViewById(R.id.lvQuizList);
        quizList = new ArrayList<>();
        quizListAdapter = new QuizListAdapter(getContext(), quizList);
        lvQuiz.setAdapter(quizListAdapter);
        quizListAdapter.notifyDataSetChanged();
        srlQuiz = (SwipeRefreshLayout) rootView.findViewById(R.id.srlQuiz);
        srlQuiz.setColorSchemeResources(R.color.swipe_refresh_color);
        srlQuiz.setOnRefreshListener(this);
        Functions.loadQuiz(sem, branch, activity, quizList, quizListAdapter, srlQuiz);
        lvQuiz.setOnItemLongClickListener(this);
        return rootView;
    }

    @Override
    public void onRefresh() {
        Functions.loadQuiz(sem, branch, activity, quizList, quizListAdapter, srlQuiz);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Quiz quiz = (Quiz) adapterView.getItemAtPosition(i);
        QuizInfoDialogFragment quizInfoDialogFragment = new QuizInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("QUIZ", quiz);
        quizInfoDialogFragment.setArguments(bundle);
        quizInfoDialogFragment.show(getFragmentManager(), "QUIZ_INFO");
        return true;
    }
}
