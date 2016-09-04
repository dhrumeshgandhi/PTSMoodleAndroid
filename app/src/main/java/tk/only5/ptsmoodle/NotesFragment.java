package tk.only5.ptsmoodle;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ProgressCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class NotesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {

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
        final Notes note = (Notes) adapterView.getItemAtPosition(i);
        ParseFile document = note.getFile();
        dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("Downloading File!");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
        document.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(final byte[] data, ParseException e) {
                DialogProperties properties = new DialogProperties();
                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                properties.selection_type = DialogConfigs.DIR_SELECT;
                properties.root = new File(DialogConfigs.DEFAULT_DIR);
                properties.extensions = null;
                FilePickerDialog filePickerDialog = new FilePickerDialog(activity, properties);
                filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(final String[] files) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    FileOutputStream fos = new FileOutputStream(new File(files[0] + "/" + note.getName()));
                                    fos.write(data);
                                    Log.d(TAG, "FILE DOWNLOADED :" + (fos.getChannel().size() / 1024) + " KB");
                                    fos.close();
                                } catch (Throwable e) {
                                    Log.d(TAG, "ERROR", e);
                                }
                            }
                        }.start();
                    }
                });
                dialog.dismiss();
                filePickerDialog.show();
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                Log.d(TAG, "FILE_DOWNLOAD_" + note.getName() + ":" + percentDone + "%");
                dialog.setProgress(percentDone);
            }
        });
        return true;
    }
}
