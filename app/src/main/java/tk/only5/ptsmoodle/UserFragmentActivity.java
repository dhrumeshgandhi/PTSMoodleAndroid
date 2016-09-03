package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UserFragmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogout;
    private Activity activity = this;
    private String TAG = InitClass.TAG;
    private RelativeLayout rlUserFragemtnActivity;
    private TextView tvName, tvExtraDetail;
    private String channel;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = Functions.showLoading(activity, "Please Wait!");
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        setContentView(R.layout.activity_user_fragment);
        final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            tvName = (TextView) findViewById(R.id.tvName);
            tvExtraDetail = (TextView) findViewById(R.id.tvExtraDetail);
            tvName.setText(user.getString("FIRST_NAME") + " " + user.getString("LAST_NAME"));
            switch (user.getString("POST")) {
                case "Teacher":
                    ParsePush.subscribeInBackground(channel = user.getString("TEACHER_ID"), new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) Log.d(TAG, "Channel Subscribed");
                            else Log.e(TAG, "ERROR", e);
                            tvExtraDetail.setText(user.getString("TEACHER_ID"));
                            Functions.setFragment(getSupportFragmentManager(), new TeacherFragment(), "TEACHER_FRAGMENT", R.id.fragmentContainerUser, false);
                        }
                    });
                    break;
                case "Parent":
                    ParsePush.subscribeInBackground(channel = "P" + user.getString("STUDENT_ENROLL"), new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) Log.d(TAG, "Channel Subscribed");
                            else Log.e(TAG, "ERROR", e);
                            tvExtraDetail.setText("");
                            Functions.setFragment(getSupportFragmentManager(), new ParentFragment(), "PARENT_FRAGMENT", R.id.fragmentContainerUser, false);
                        }
                    });
                    break;
                case "Student":
                    ParsePush.subscribeInBackground(channel = user.getString("ENROLLMENT"), new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) Log.d(TAG, "Channel Subscribed");
                            else Log.e(TAG, "ERROR", e);
                            tvExtraDetail.setText(user.getString("ENROLLMENT"));
                            Functions.setFragment(getSupportFragmentManager(), new StudentFragment(), "STUDENT_FRAGMENT", R.id.fragmentContainerUser, false);

                        }
                    });
                    break;
            }
            loadingDialog.dismiss();
        } else {
            ParseUser.logOutInBackground();
            startActivity(new Intent(UserFragmentActivity.this, LoginSignUpFragmentActivity.class));
            finish();
        }
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        rlUserFragemtnActivity = (RelativeLayout) findViewById(R.id.rlUserFragmentActivity);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnLogout)) {
            loadingDialog = Functions.showLoading(activity, "Logging Out!");
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Logout Success");
                        ParsePush.unsubscribeInBackground(channel, new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) Log.d(TAG, "Channel Unsubscribed");
                                else Log.e(TAG, "ERROR", e);
                            }
                        });
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Log.d(TAG, "popping backstack");
                        fragmentManager.popBackStack();
                        loadingDialog.dismiss();
                        startActivity(new Intent(UserFragmentActivity.this, LoginSignUpFragmentActivity.class));
                        finish();
                    } else {
                        Log.e(TAG, "Logout Failed", e);
                        Snackbar.make(rlUserFragemtnActivity, R.string.error_technical, Snackbar.LENGTH_LONG).show();
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
     /*   FragmentManager fragmentManager = getSupportFragmentManager();
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
