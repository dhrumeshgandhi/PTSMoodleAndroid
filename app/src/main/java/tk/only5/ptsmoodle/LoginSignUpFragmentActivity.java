package tk.only5.ptsmoodle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class LoginSignUpFragmentActivity extends AppCompatActivity {

    private String TAG = InitClass.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup_fragment);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
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
