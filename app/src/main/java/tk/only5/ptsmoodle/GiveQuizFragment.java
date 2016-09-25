package tk.only5.ptsmoodle;


import android.app.Activity;
import android.app.ProgressDialog;
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
    private SwipeRefreshLayout srlNotes;
    private ListView lvNotes;
    private NotesListAdapter notesListAdapter;
    private ArrayList<Notes> notesList;
    private String sem, branch;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        activity = getActivity();
        activity.setTitle("NOTES");
        sem = getArguments().getString("SEM");
        branch = getArguments().getString("BRANCH");
        lvNotes = (ListView) rootView.findViewById(R.id.lvNotes);
        notesList = new ArrayList<>();
        notesListAdapter = new NotesListAdapter(getContext(), notesList);
        lvNotes.setAdapter(notesListAdapter);
        notesListAdapter.notifyDataSetChanged();
        srlNotes = (SwipeRefreshLayout) rootView.findViewById(R.id.srlNotes);
        srlNotes.setColorSchemeResources(R.color.swipe_refresh_color);
        srlNotes.setOnRefreshListener(this);
        Functions.loadNotes(sem, branch, activity, notesList, notesListAdapter, srlNotes);
        lvNotes.setOnItemLongClickListener(this);
        return rootView;
    }

    @Override
    public void onRefresh() {
        Functions.loadNotes(sem, branch, activity, notesList, notesListAdapter, srlNotes);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return true;
    }
}
