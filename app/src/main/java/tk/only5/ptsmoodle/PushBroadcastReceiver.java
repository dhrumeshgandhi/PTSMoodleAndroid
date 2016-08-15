package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DHRUMESH on 7/24/2016.
 */
public class PushBroadcastReceiver extends ParsePushBroadcastReceiver {
    private static String TAG = InitClass.TAG;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        Log.d(TAG, "Push Received" + intent.getExtras().getString("com.parse.Data") + " on channels " + intent.getStringExtra("com.parse.channel"));
        try {
            JSONObject pushData = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Map<String, String> pushDetails = new HashMap<>();
            pushDetails.put("title", pushData.getString("title"));
            pushDetails.put("alert", pushData.getString("alert"));
            pushDetails.put("channels", intent.getStringExtra("com.parse.channel"));
            pushDetails.put("time", (new Date(System.currentTimeMillis())).toString());
            FlurryAgent.logEvent("PushReceived", pushDetails);
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        try {
            JSONObject pushData = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Map<String, String> pushDetails = new HashMap<>();
            pushDetails.put("title", pushData.getString("title"));
            pushDetails.put("alert", pushData.getString("alert"));
            pushDetails.put("channels", intent.getStringExtra("com.parse.channel"));
            pushDetails.put("time", (new Date(System.currentTimeMillis())).toString());
            FlurryAgent.logEvent("PushOpened", pushDetails);
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }
    }

    @Override
    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return super.getActivity(context, intent);
    }

    @Override
    protected int getSmallIconId(Context context, Intent intent) {
        return super.getSmallIconId(context, intent);
    }

    @Override
    protected Bitmap getLargeIcon(Context context, Intent intent) {
        return super.getLargeIcon(context, intent);
    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        return super.getNotification(context, intent);
    }
}
