package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

public class AddAttendanceDialogFragment extends DialogFragment implements View.OnClickListener {

    protected static CustomViewPager viewPager;
    protected static FloatingActionButton fabCancel, fabNext, fabPrevious;
    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private ParseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_add_attendance, container, false);
        activity = getActivity();
        getDialog().setTitle("Add Attendance");
        // Functions.sendNotification("Test", "Hello", Arrays.asList("13012011012", "TEST"));
        user = ParseUser.getCurrentUser();
        fabNext = (FloatingActionButton) rootView.findViewById(R.id.fabNext);
        fabPrevious = (FloatingActionButton) rootView.findViewById(R.id.fabPrevious);
        fabCancel = (FloatingActionButton) rootView.findViewById(R.id.fabCancel);
        fabNext.setOnClickListener(this);
        fabCancel.setOnClickListener(this);
        fabPrevious.setOnClickListener(this);
        viewPager = (CustomViewPager) rootView.findViewById(R.id.vpAddAttendanceDialog);
        viewPager.setAdapter(new AddAttendanceDialogViewPagerAdapter(getChildFragmentManager()));
        AddAttendanceDialogFragment.fabPrevious.setVisibility(View.INVISIBLE);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(fabNext)) {
            int current = viewPager.getCurrentItem();
            if (current == 0) AddAttendanceDialogViewsFragment.getAttendanceInfo();
            else if (current == 1) AddAttendanceDialogViewsFragment.addAttendance();
            viewPager.setCurrentItem(current + 1);
            if (viewPager.getCurrentItem() == viewPager.getChildCount())
                hideNextPreviousButtons(true, true);
            else
                hideNextPreviousButtons(false, false);

        } else if (view.equals(fabPrevious)) {
            int current = viewPager.getCurrentItem();
            viewPager.setCurrentItem(current - 1);
            if (viewPager.getCurrentItem() == 0)
                hideNextPreviousButtons(false, true);
            else
                hideNextPreviousButtons(false, false);
        } else if (view.equals(fabCancel)) {
            viewPager.setCurrentItem(0);
            this.dismiss();
        }
    }

    private void hideNextPreviousButtons(boolean next, boolean previous) {
        if (next) {
            fabNext.setVisibility(View.INVISIBLE);
        } else {
            fabNext.setVisibility(View.VISIBLE);
        }
        if (previous) {
            fabPrevious.setVisibility(View.INVISIBLE);
        } else {
            fabPrevious.setVisibility(View.VISIBLE);
        }
    }
}
