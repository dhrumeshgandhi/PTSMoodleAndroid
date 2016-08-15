package tk.only5.ptsmoodle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SplashActivity extends AppCompatActivity {

    private String TAG = InitClass.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Functions.getConfig();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    Intent intent = new Intent(SplashActivity.this,
                            LoginSignUpFragmentActivity.class);
                    Log.d(TAG, "Not Logged In!");
                    startActivity(intent);
                    finish();
                } else {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        // Send logged in users to Welcome.class
                        Intent intent = new Intent(SplashActivity.this, UserFragmentActivity.class);
                        Log.d(TAG, "Logged In!");
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this,
                                LoginSignUpFragmentActivity.class);
                        Log.d(TAG, "Not Logged In!");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 3000);
    }
}
