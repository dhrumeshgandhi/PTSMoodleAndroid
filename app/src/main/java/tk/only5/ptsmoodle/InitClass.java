package tk.only5.ptsmoodle;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Created by DHRUMESH on 7/23/2016.
 */
public class InitClass extends Application {
    protected static String TAG = "PTS_TAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Parse.initialize(new Parse.Configuration.Builder(this)
                //.server("http://192.168.0.103:1337/parse/")
                //.applicationId("app123")
                //.clientKey(null)
                .server("https://parseapi.back4app.com/")
                .applicationId("jfj5WUR8w9gXg8HP4aqYUsVfgLs99oXBZ3teMshs")
                .clientKey("on0mDuCrzGU8VlDjtA5Bg8fnrNPz8ELQTT2mpnRm")
                .build());
        ParseUser.enableAutomaticUser();
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(getApplicationContext(), "TQMCT6YDRH2PP44HSW3C");
    }


}
