package tk.only5.ptsmoodle;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;

public class NotificationUserSelectionDialogFragment extends DialogFragment implements View.OnClickListener {

    protected static CustomViewPager viewPager;
    protected static Button btnNext, btnPrevious;
    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private ParseUser user;
    private FloatingActionButton fabCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification_user_selection_dialog_fragement, container, false);
        activity = getActivity();
        getDialog().setTitle("Send Notification");
        user = ParseUser.getCurrentUser();
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnPrevious = (Button) rootView.findViewById(R.id.btnPrevious);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        viewPager = (CustomViewPager) rootView.findViewById(R.id.vpNotificationUserSelectionDialog);
        viewPager.setAdapter(new NotificationUserSelectionDialogViewPagerAdapter(getChildFragmentManager()));
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnNext)) {
            int current = viewPager.getCurrentItem();
            viewPager.setCurrentItem(current + 1);
        } else if (view.equals(btnPrevious)) {
            int current = viewPager.getCurrentItem();
            viewPager.setCurrentItem(current - 1);
        } else if (view.equals(btnCancel)) {
            this.dismiss();
        }
    }

}
