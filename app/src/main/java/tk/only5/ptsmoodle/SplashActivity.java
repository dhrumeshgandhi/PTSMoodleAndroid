package tk.only5.ptsmoodle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {

    private String TAG = InitClass.TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Functions.getConfig();
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
