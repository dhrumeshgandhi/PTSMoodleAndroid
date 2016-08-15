package tk.only5.ptsmoodle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class LoginSignUpFragmentActivity extends AppCompatActivity {

    private String TAG = InitClass.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_fragment);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d(TAG, ParseInstallation.getCurrentInstallation().getString("deviceToken") + "");
                if (e == null) {
                    Log.d(TAG, "Installation Saved");
                    ParsePush.subscribeInBackground("TEST", new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) Log.d(TAG, "Channel Subscribed");
                            else Log.e(TAG, "ERROR", e);
                        }
                    });
                } else Log.e(TAG, "ERROR", e);
            }
        });
        Functions.setFragment(getSupportFragmentManager(), new LoginFragment(), getResources().getString(R.string.label_login), R.id.fragmentContainerLoginSignUp, false);

    }

    @Override
    public void onBackPressed() {
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.d(TAG, "popping backstack");
            fragmentManager.popBackStack();
        } else {
            Log.d(TAG, "nothing on backstack, calling super");
            super.onBackPressed();
        }*/
        super.onBackPressed();
    }
}
