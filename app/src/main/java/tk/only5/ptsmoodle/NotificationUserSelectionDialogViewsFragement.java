package tk.only5.ptsmoodle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class NotificationUserSelectionDialogViewsFragement extends Fragment implements AdapterView.OnItemSelectedListener {

    int pos;
    private View rootView;
    private Spinner spinUserSelection;
    private ArrayAdapter<CharSequence> userSelectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pos = getArguments().getInt("POS");
        rootView = inflater.inflate(
                R.layout.fragment_notification_user_selection_dialog_view1_fragement,
                container, false);
        switch (pos) {
            case 0:
                spinUserSelection = (Spinner) rootView.findViewById(R.id.spinUserSelection);
                userSelectionAdapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.notification_user_selection_list,
                        android.R.layout.simple_spinner_dropdown_item);
                spinUserSelection.setAdapter(userSelectionAdapter);
                NotificationUserSelectionDialogFragment.btnPrevious.setVisibility(View.INVISIBLE);
                break;
            case 1:
                break;
            case 2:
                break;
        }
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
