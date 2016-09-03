package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.parse.ConfigCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    protected static int FILE_PICK_REQUEST_CODE = 11111;
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

    protected static void sendNotification(String title, String message, List<String> channels) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("title", title);
            params.put("message", message);
            params.put("channels", channels);
            ParseCloud.callFunctionInBackground("sendPush", params, new FunctionCallback<String>() {
                @Override
                public void done(String message, ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Function Call Success:" + message);
                    } else {
                        Log.e(TAG, "ERROR: " + message, e);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
        }
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
                        }
                    });
                } else {
                    Log.e(TAG, "Query unsuccessful", e);
                }
                srlNotification.setRefreshing(false);
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

    protected static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    protected static void uploadFile(String path, final String topic, final Activity activity, final String uploadedBy, final ProgressDialog dialog) throws Exception {
        final String fileName = path.substring(path.lastIndexOf('/') + 1).replace(" ", "_");
        Log.d(TAG, fileName);
        dialog.setMessage(fileName);
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] file = new byte[(int) fis.getChannel().size()];
        fis.read(file);
        final ParseFile parseFile = new ParseFile(fileName, file);
        Log.d(TAG, "FILE_UPLOAD_STARTED:" + path);
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "FILE UPLOADED:" + parseFile.getUrl());
                    ParseObject uploadDoc = new ParseObject("UploadedFiles");
                    uploadDoc.put("NAME", fileName);
                    uploadDoc.put("UPLOADED_BY", uploadedBy);
                    uploadDoc.put("UPLOAD_DATE", getCurrentDateTime());
                    uploadDoc.put("TOPIC", topic);
                    uploadDoc.put("DOCUMENT", parseFile);
                    uploadDoc.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(TAG, "FileObjectSaved");
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Snackbar.make(activity.findViewById(R.id.fragmentContainerUser), "FILE UPLOADED:" + fileName, Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            } else
                                Log.e(TAG, "ERROR", e);
                        }
                    });
                    dialog.dismiss();
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(activity.findViewById(R.id.fragmentContainerUser), R.string.error_technical, Snackbar.LENGTH_LONG).show();
                        }
                    });
                    dialog.dismiss();
                    Log.e(TAG, "ERROR:", e);
                }
                // dialog.dismiss();
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                Log.d(TAG, "FILE_UPLOAD_" + fileName + ":" + percentDone + "%");
                dialog.setProgress(percentDone);
            }
        });
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
