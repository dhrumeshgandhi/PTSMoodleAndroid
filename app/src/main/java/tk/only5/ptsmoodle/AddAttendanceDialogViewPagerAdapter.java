package tk.only5.ptsmoodle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by DHRUMESH on 8/19/2016.
 */
public class AddAttendanceDialogViewPagerAdapter extends FragmentStatePagerAdapter {

    AddAttendanceDialogViewsFragment
            addAttendanceDialogViewsFragment;
    Bundle bundle;

    public AddAttendanceDialogViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                addAttendanceDialogViewsFragment = new AddAttendanceDialogViewsFragment();
                bundle = new Bundle();
                bundle.putInt("POS", position);
                //  NotificationUserSelectionDialogFragment.fabPrevious.setVisibility(View.INVISIBLE);
                //   NotificationUserSelectionDialogFragment.fabNext.setVisibility(View.VISIBLE);
                break;
            case 1:
                addAttendanceDialogViewsFragment = new AddAttendanceDialogViewsFragment();
                bundle = new Bundle();
                bundle.putInt("POS", position);
                //    NotificationUserSelectionDialogFragment.fabPrevious.setVisibility(View.VISIBLE);
                //    NotificationUserSelectionDialogFragment.fabNext.setVisibility(View.VISIBLE);
                break;
            case 2:
                addAttendanceDialogViewsFragment = new AddAttendanceDialogViewsFragment();
                bundle = new Bundle();
                bundle.putInt("POS", position);
                //    NotificationUserSelectionDialogFragment.fabNext.setVisibility(View.INVISIBLE);
                //   NotificationUserSelectionDialogFragment.fabPrevious.setVisibility(View.VISIBLE);
                break;
        }
        addAttendanceDialogViewsFragment.setArguments(bundle);
        return addAttendanceDialogViewsFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
