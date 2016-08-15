package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.parse.ConfigCallback;
import com.parse.FindCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by DHRUMESH on 7/23/2016.
 */
public class Functions {
    protected static ArrayList<String> SEMESTERS = new ArrayList<>(), BRANCHES = new ArrayList<>();
    protected static FragmentManager currentFragmentManager;
    private static String TAG = InitClass.TAG;
    private static ProgressDialog dialog;
    private static View[] views;

    protected static View[] getChildFragmentViews() {
        return views;
    }

    protected static void setChildFragmentViews(View[] viewArray) {
      /*  switch (post) {
            case "Teacher":
                etRegisterTeacherId = (EditText) views[0];
                lvTeacherExtraDetailsAddClassSubject = (ListView) views[1];
                break;
            case "Student":
                etRegisterStudentEnroll = (EditText) views[0];
                spinStudentSem = (Spinner) views[1];
                spinStudentBranch = (Spinner) views[2];
                break;
            case "Parent":
                etRegisterStudentName = (EditText) views[0];
                etRegisterStudentEnroll = (EditText) views[1];
                break;
        }*/
        views = viewArray;
    }

    protected static void getConfig() {
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Config Fetched!");
                } else {
                    Log.e(TAG, "Error: Using Cached Config", e);
                    config = ParseConfig.getCurrentConfig();
                }
                if (config.get("Semesters") != null) {
                    SEMESTERS = (ArrayList<String>) config.get("Semesters");
                    BRANCHES = (ArrayList<String>) config.get("Branches");
                } else {
                    for (int i = 1; i < 9; i++) {
                        SEMESTERS.add(i - 1, i + "");
                    }
                    BRANCHES.add(0, "CE");
                    BRANCHES.add(1, "IT");
                }
            }
        });
    }

    protected static void sendPush(String title, String message, JSONArray channels) {
        Map<String, Object> pushDetails = new HashMap<>();
        pushDetails.put("title", title);
        pushDetails.put("message", message);
        pushDetails.put("channels", channels);
    }

    protected static void setFragment(FragmentManager fragmentManager,
                                      Fragment fragment, String fragmentTag,
                                      int containerId, boolean addtoBackStack) {
        if (fragmentManager == null) fragmentManager = currentFragmentManager;
        else currentFragmentManager = fragmentManager;
        FragmentTransaction fragmenttransaction = fragmentManager.beginTransaction();
        fragmenttransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        if (addtoBackStack) fragmenttransaction.addToBackStack(fragmentTag);
        fragmenttransaction.replace(containerId, fragment, fragmentTag).commit();
        Log.d(TAG, "Current Fragment : " + fragmentTag);
    }

    protected static ProgressDialog showLoading(Activity activity, String msg) {
        dialog = new ProgressDialog(activity);
        dialog.setTitle(msg);
        dialog.setMessage("Please Wait!");
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    protected static void hideSoftKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    protected static void loadNotifications(String notificationFor, final Activity activity,
                                            final ArrayList<Notification> notificationList,
                                            final NotificationListAdapter notificationListAdapter,
                                            final SwipeRefreshLayout srlNotification) {
        srlNotification.setRefreshing(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
        query.addDescendingOrder("updatedAt");
        query.whereEqualTo("SENT_TO", notificationFor);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Query successful");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notificationList.clear();
                            notificationListAdapter.notifyDataSetChanged();
                            for (ParseObject object : objects) {
                                notificationList.add(new Notification(object.getString("TITLE"), object.getString("DATE_TIME"), object.getString("MESSAGE")));
                                notificationListAdapter.notifyDataSetChanged();
                            }
                            srlNotification.setRefreshing(false);
                        }
                    });
                } else {
                    Log.e(TAG, "Query unsuccessful", e);
                }
            }
        });
    }

    protected static void readExcelFile(final String path, final int noOfQues) {
        new Thread() {
            @Override
            public void run() {
                int cells = 0, rows = 0;
                try {
                    InputStream excelFile = new FileInputStream(path);
                    HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    Iterator rowIterator = sheet.rowIterator();
                    Iterator cellIterator;
                    HSSFRow row;
                    HSSFCell cell;
                    if (rowIterator.hasNext()) {
                        rowIterator.next();
                        while (rowIterator.hasNext() && rows < noOfQues) {
                            cells = 0;
                            rows++;
                            row = (HSSFRow) rowIterator.next();
                            cellIterator = row.cellIterator();
                            while (cellIterator.hasNext() && cells < 6) {
                                cell = (HSSFCell) cellIterator.next();
                                Log.d(TAG, "CELL :" + cell.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error", e);
                }
            }
        }.start();
    }
    //For File Pick Dialog ...Add following to activity/fragment

      /*  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 5);*/
    //And add following
    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5) {
            try {
                String path = data.getData().getPath();
                //String path="/data/data/tk.only5.quizapp/Questions.xls";
                Log.d(TAG, "LOCATION:" + path);
                Functions.readExcelFile(path, 10);
            } catch (Exception e) {
                Log.e(TAG, "Error", e);
            }
        }
    }
     */

}
