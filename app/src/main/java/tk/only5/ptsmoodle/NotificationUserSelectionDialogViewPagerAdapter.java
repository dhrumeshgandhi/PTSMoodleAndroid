package tk.only5.ptsmoodle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by DHRUMESH on 8/19/2016.
 */
public class NotificationUserSelectionDialogViewPagerAdapter extends FragmentStatePagerAdapter {

    public NotificationUserSelectionDialogViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        NotificationUserSelectionDialogViewsFragement
                notificationUserSelectionDialogViewsFragement
                = new NotificationUserSelectionDialogViewsFragement();
        Bundle bundle = new Bundle();
        bundle.putInt("POS", position);
        notificationUserSelectionDialogViewsFragement.setArguments(bundle);
        return notificationUserSelectionDialogViewsFragement;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
