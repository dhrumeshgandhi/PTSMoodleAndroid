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
import android.widget.Toast;

import com.parse.ConfigCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Functions {
    protected static ArrayList<String> SEMESTERS = new ArrayList<>(), BRANCHES = new ArrayList<>(), SEM_TEACHER = new ArrayList<>(), BRANCH_TEACHER = new ArrayList<>();
    protected static FragmentManager currentFragmentManager;
    private static String TAG = InitClass.TAG;
    private static ProgressDialog dialog;
    private static View[] views;
    private static int correct, wrong;
    private static double marks;

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
        query.addDescendingOrder("DATE_TIME");
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

    protected static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return dateFormat.format(new Date());
    }

    protected static void uploadFile(String path, final String subject, final String sem,
                                     final String branch, final Activity activity,
                                     final ParseUser user, final ProgressDialog dialog)
            throws Exception {
        final String fileName = path.substring(path.lastIndexOf('/') + 1).replace(" ", "_");
        final String uploadedBy = user.getString("TEACHER_ID");
        final String teacherName = user.getString("FIRST_NAME") + " " + user.getString("LAST_NAME");
        Log.d(TAG, fileName);
        dialog.setMessage(fileName);
        FileInputStream fis = new FileInputStream(new File(path));
        Log.d(TAG, "FILE SIZE:" + (((double) fis.getChannel().size()) / 1024) + " KB");
        byte[] file = new byte[(int) fis.getChannel().size()];
        fis.read(file);
        final ParseFile parseFile = new ParseFile(fileName, file);
        Log.d(TAG, "FILE_UPLOAD_STARTED:" + path);
        fis.close();
        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "FILE UPLOADED:" + parseFile.getUrl());
                    ParseObject uploadDoc = new ParseObject("UploadedFiles");
                    uploadDoc.put("NAME", fileName);
                    uploadDoc.put("UPLOADED_BY", uploadedBy);
                    uploadDoc.put("UPLOAD_DATE", getCurrentDateTime());
                    uploadDoc.put("SUBJECT", subject);
                    uploadDoc.put("BRANCH", branch);
                    uploadDoc.put("SEMESTER", sem);
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
                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                query.whereEqualTo("BRANCH", branch);
                                query.whereEqualTo("SEMESTER", sem);
                                query.whereEqualTo("POST", "Student");
                                query.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> objects, ParseException e) {
                                        List<String> channels = new ArrayList<String>();
                                        if (e == null) {
                                            String title = activity.getString(
                                                    R.string.notification_title_note_uploaded);
                                            String message = activity.getString(
                                                    R.string.notification_message_note_uploaded)
                                                    .replace("#TEACHER#", teacherName)
                                                    .replace("#SUBJECT#", subject);
                                            for (ParseUser user : objects) {
                                                Log.d(TAG, user.getEmail());
                                                channels.add(user.getString("ENROLLMENT"));
                                            }
                                            if (channels.size() > 0) {
                                                sendNotification(title, message, channels);
                                            }
                                            addNotificationToList(title, message, channels, uploadedBy);
                                        } else Log.e(TAG, "ERROR", e);
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

    protected static void addNotificationToList(String title, String message, List<String> sentTo, String sentBy) {
        ParseObject notification = new ParseObject("Notification");
        notification.put("TITLE", title);
        notification.put("MESSAGE", message);
        notification.put("DATE_TIME", getCurrentDateTime());
        notification.put("SENT_TO", sentTo);
        notification.put("SENT_BY", sentBy);
        notification.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) Log.d(TAG, "NOTIFICATION ADDED!");
                else Log.e(TAG, "ERROR", e);
            }
        });
    }

    protected static void getTeacherClassSubjects(String teacherID) {
        final ArrayList<ExtraDetailsTeacherClassSubject> classSubjects = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("TEACHER_ID", teacherID);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    new Thread() {
                        @Override
                        public void run() {
                            for (int i = 0; i < objects.size(); i++) {
                                try {
                                    Log.d(TAG, "Number of Users : " + objects.size());
                                    ArrayList<HashMap> arrayList = (ArrayList<HashMap>) objects.get(i).get("CLASS_SUBJECT");
                                    for (int j = 0; j < arrayList.size(); j++) {
                                        Log.d(TAG, "NUMBER of Classes:" + arrayList.size());
                                        classSubjects.add(new ExtraDetailsTeacherClassSubject(arrayList.get(j)));
                                        //return classSubjects;
                                    }
                                    for (int k = 0; k < classSubjects.size(); k++) {
                                        Log.d(TAG, classSubjects.get(k).getSemItem() + " " + classSubjects.get(k).getBranchItem());
                                        if (!SEM_TEACHER.contains(classSubjects.get(k).getSemItem()))
                                            SEM_TEACHER.add(classSubjects.get(k).getSemItem());
                                        if (!BRANCH_TEACHER.contains(classSubjects.get(k).getBranchItem()))
                                            BRANCH_TEACHER.add(classSubjects.get(k).getBranchItem());
                                    }
                                } catch (Exception ex) {
                                    Log.e(TAG, "ERROR", ex);
                                }
                            }
                        }
                    }.start();

                } else {
                    Log.e(TAG, "ERROR:", e);
                }
            }
        });
    }

    protected static void loadNotes(String sem, String branch, final Activity activity,
                                    final ArrayList<Notes> notesList, final NotesListAdapter notesListAdapter,
                                    final SwipeRefreshLayout srlNotes) {
        srlNotes.setRefreshing(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UploadedFiles");
        query.whereEqualTo("SEMESTER", sem);
        query.whereEqualTo("BRANCH", branch);
        query.addDescendingOrder("UPLOAD_DATE");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Query successful");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notesList.clear();
                            notesListAdapter.notifyDataSetChanged();
                            for (final ParseObject object : objects) {

                                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                                userQuery.whereEqualTo("TEACHER_ID", object.getString("UPLOADED_BY"));
                                try {
                                    ParseUser teacher = userQuery.getFirst();
                                    String teacherName =
                                            teacher.getString("FIRST_NAME") + " " +
                                                    teacher.getString("LAST_NAME");
                                    notesList.add(new Notes(object.getString("NAME"),
                                            teacherName, object.getString("UPLOAD_DATE"),
                                            object.getString("SUBJECT"), object.getParseFile("DOCUMENT")));
                                    notesListAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Log.e(TAG, "ERROR", e);
                                }
                                srlNotes.setRefreshing(false);
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Query Unsuccessful", e);
                    srlNotes.setRefreshing(false);
                }
            }
        });

    }

    protected static JSONArray getQuestionsInJson(ArrayList<Question> queList) {
        String id;
        JSONArray questions = new JSONArray();
        JSONObject question;
        try {
            for (int i = 0; i < queList.size(); i++) {
                id = (i + 1) + "";
                question = new JSONObject();
                question.put("ID", id);
                question.put("QUESTION", queList.get(i).getQue());
                question.put("ANSWER", queList.get(i).getAns());
                question.put("OPTION1", queList.get(i).getOpt1());
                question.put("OPTION2", queList.get(i).getOpt2());
                question.put("OPTION3", queList.get(i).getOpt3());
                questions.put(i, question);
                question = new JSONObject();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
        Log.d(TAG, questions.toString());
        return questions;
    }

    protected static void readExcelFile(final Activity activity, final String path, final Quiz quizData) {
        final ArrayList<Question> quesList = new ArrayList<>();
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
                    String[] que;
                    if (rowIterator.hasNext()) {
                        rowIterator.next();
                        while (rowIterator.hasNext() && rows < sheet.getLastRowNum()) {
                            cells = 0;
                            rows++;
                            row = (HSSFRow) rowIterator.next();
                            que = new String[6];
                            cellIterator = row.cellIterator();
                            while (cellIterator.hasNext() && cells < 6) {
                                cell = (HSSFCell) cellIterator.next();
                                que[cells++] = cell.toString();
                                // Log.d(TAG, "CELL :" + cell.toString());
                            }
                            quesList.add(new Question(que[1], que[2], que[3], que[4], que[5], que[0]));
                        }

                        JSONArray jsonQues = getQuestionsInJson(quesList);
                        uploadQuiz(activity, quizData, jsonQues);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error", e);
                }
            }
        }.start();
    }

    private static void uploadQuiz(final Activity activity, final Quiz quizData, JSONArray jsonQues) {
        final ParseObject quiz = new ParseObject("Quiz");
        quiz.put("NAME", quizData.getName());
        quiz.put("SUBJECT", quizData.getSubject());
        quiz.put("NO_OF_QUESTIONS", quizData.getNo_of_ques());
        quiz.put("POSITIVE_MARKS", quizData.getPositive_marks());
        quiz.put("NEGATIVE_MARKS", quizData.getNegative_marks());
        quiz.put("TIME_LIMIT", quizData.getTime_limit());
        quiz.put("TEACHER_ID", quizData.getTeacher_id());
        quiz.put("BRANCH", quizData.getBranch());
        quiz.put("SEMESTER", quizData.getSem());
        quiz.put("DATE_TIME", getCurrentDateTime());
        quiz.put("QUESTIONS", jsonQues);
        quiz.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "QUIZ UPLOADED!");
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("BRANCH", quizData.getBranch());
                    query.whereEqualTo("SEMESTER", quizData.getSem());
                    query.whereEqualTo("POST", "Student");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            List<String> channels = new ArrayList<String>();
                            if (e == null) {
                                String title = activity.getString(R.string.notification_title_quiz_uploaded);
                                String message = activity.getString(R.string.notification_message_quiz_uploaded)
                                        .replace("#NAME#", quizData.getName())
                                        .replace("#SUBJECT#", quizData.getSubject())
                                        .replace("#TEACHER#", quizData.getTeacher_id());
                                for (ParseUser user : objects) {
                                    Log.d(TAG, user.getEmail());
                                    channels.add(user.getString("ENROLLMENT"));
                                }
                                if (channels.size() > 0) {
                                    sendNotification(title, message, channels);
                                }
                                addNotificationToList(title, message, channels, quizData.getTeacher_id());
                            } else Log.e(TAG, "ERROR", e);
                        }
                    });
                    Toast.makeText(activity, "Quiz Uploaded", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(TAG, "QUIZ UPLOAD ERROR", e);
                }
            }
        });
    }

    protected static void loadAndUploadQuiz(Activity activity, String path, Quiz quizData) {
        readExcelFile(activity, path, quizData);
    }

    protected static void loadQuiz(String sem, String branch, final Activity activity,
                                   final ArrayList<Quiz> quizList, final QuizListAdapter quizListAdapter,
                                   final SwipeRefreshLayout srlNotes) {
        srlNotes.setRefreshing(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Quiz");
        query.whereEqualTo("SEMESTER", sem);
        query.whereEqualTo("BRANCH", branch);
        query.addDescendingOrder("DATE_TIME");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Query successful");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            quizList.clear();
                            quizListAdapter.notifyDataSetChanged();
                            for (final ParseObject object : objects) {
                                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                                userQuery.whereEqualTo("TEACHER_ID", object.getString("TEACHER_ID"));
                                try {
                                    ParseUser teacher = userQuery.getFirst();
                                    String teacherName =
                                            teacher.getString("FIRST_NAME") + " " +
                                                    teacher.getString("LAST_NAME");
                                    Quiz quiz = new Quiz(
                                            object.getString("NAME"),
                                            object.getString("SUBJECT"),
                                            object.getString("NO_OF_QUESTIONS"),
                                            object.getString("POSITIVE_MARKS"),
                                            object.getString("NEGATIVE_MARKS"),
                                            object.getString("TIME_LIMIT"),
                                            teacherName,
                                            object.getString("BRANCH"),
                                            object.getString("SEMESTER"),
                                            object.getString("DATE_TIME"));
                                    quiz.setTeacher_name(teacherName);
                                    quiz.setQuestions(object.getJSONArray("QUESTIONS"));
                                    quizList.add(quiz);
                                    quizListAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    Log.e(TAG, "ERROR", e);
                                }
                            }
                            srlNotes.setRefreshing(false);
                        }
                    });

                } else {
                    Log.e(TAG, "Query Unsuccessful", e);
                    srlNotes.setRefreshing(false);
                }
            }
        });

    }

    protected static ArrayList<Question> jsonQuestionsToRandomizedQuesList(JSONArray jsonQues) {
        ArrayList<Question> queList = new ArrayList<>();
        JSONObject jsonObj;
        ArrayList<String> options;
        Question question;
        try {
            for (int i = 0; i < jsonQues.length(); i++) {
                jsonObj = jsonQues.getJSONObject(i);
                options = getRandomizedOptionsFromJSON(jsonObj);
                Thread.sleep(500);
                question = new Question(
                        jsonObj.getString("QUESTION"),
                        options.get(0),
                        options.get(1),
                        options.get(2),
                        options.get(3),
                        jsonObj.getString("ID")
                );
                queList.add(question);
            }
            Collections.shuffle(queList);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
        //Log.d(TAG, getQuizInJson(new Quiz("A","A","A","A","A","A","A","A"),queList).toString());
        return queList;
    }

    protected static ArrayList<Question> jsonQuestionsToQuesList(JSONArray jsonQues) {
        ArrayList<Question> queList = new ArrayList<>();
        JSONObject jsonObj;
        ArrayList<String> options;
        Question question;
        try {
            for (int i = 0; i < jsonQues.length(); i++) {
                jsonObj = jsonQues.getJSONObject(i);
                options = getOptionsFromJSON(jsonObj);
                Thread.sleep(500);
                question = new Question(
                        jsonObj.getString("QUESTION"),
                        options.get(0),
                        options.get(1),
                        options.get(2),
                        options.get(3),
                        jsonObj.getString("ID")
                );
                queList.add(question);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
        //Log.d(TAG, getQuizInJson(new Quiz("A","A","A","A","A","A","A","A"),queList).toString());
        return queList;
    }

    protected static ArrayList<String> getRandomizedOptionsFromJSON(JSONObject jsonObj) throws JSONException {
        ArrayList<String> options = new ArrayList<>();
        options.add(jsonObj.getString("ANSWER"));
        options.add(jsonObj.getString("OPTION1"));
        options.add(jsonObj.getString("OPTION2"));
        options.add(jsonObj.getString("OPTION3"));
        Collections.shuffle(options);
        return options;
    }

    protected static ArrayList<String> getOptionsFromJSON(JSONObject jsonObj) throws JSONException {
        ArrayList<String> options = new ArrayList<>();
        options.add(jsonObj.getString("ANSWER"));
        options.add(jsonObj.getString("OPTION1"));
        options.add(jsonObj.getString("OPTION2"));
        options.add(jsonObj.getString("OPTION3"));
        return options;
    }

    protected static void checkQuizAndUploadResult(final Fragment fragment, final ArrayList<Question> questions, final ArrayList<Answer> answers, final String positive, final String negative, final Quiz quiz, final ParseUser user, final long time_left) {
        new Thread() {
            @Override
            public void run() {
                final OnMarksCalculatedListener onMarksCalculatedListener = (OnMarksCalculatedListener) fragment;
                correct = 0;
                wrong = 0;
                marks = 0;
                for (Question question : questions) {
                    for (Answer answer : answers) {
                        if (question.getQue().equals(answer.getQue())) {
                            //   Log.d(TAG, "QUE_ANSWER:" + question.getAns());
                            //   Log.d(TAG, "ANS_ANSWER:" + answer.getAns());
                            if (question.getAns().equals(answer.getAns())) {
                                correct++;
                            } else {
                                wrong++;
                            }
                            break;
                        }
                    }
                }
                Log.d(TAG, "CORRECT:" + correct + " WRONG:" + wrong);
                marks = correct * Double.parseDouble(positive) - wrong * Double.parseDouble(negative);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("QuizResults");
                query.whereEqualTo("TITLE", quiz.getName());
                query.whereEqualTo("SUBJECT", quiz.getSubject());
                query.whereEqualTo("SEMESTER", quiz.getSem());
                query.whereEqualTo("BRANCH", quiz.getBranch());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        try {
                            if (e == null) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("ENROLLMENT", user.getString("ENROLLMENT"));
                                jsonObject.put("MARKS", marks);
                                jsonObject.put("TIME_TAKEN", getTimeFromMillis(Integer.parseInt(quiz.getTime_limit()) * 60000 - time_left));
                                jsonObject.put("CORRECT", correct);
                                jsonObject.put("WRONG", wrong);
                                JSONArray jsonArray = object.getJSONArray("RESULTS");
                                Log.d(TAG, jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if (jsonArray.getJSONObject(i).getString("ENROLLMENT").equals(user.getString("ENROLLMENT"))) {
                                        Log.d(TAG, "Quiz already submitted..SKIPPING IT!");
                                        return;
                                    }
                                }
                                jsonArray.put(jsonObject);
                                object.put("RESULTS", jsonArray);
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.d(TAG, "Result Added to list");
                                        } else {
                                            Log.e(TAG, "ERROR", e);
                                        }
                                    }
                                });
                            } else {
                                if (e.getCode() == 101) {
                                    JSONArray jsonArray = new JSONArray();
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("ENROLLMENT", user.getString("ENROLLMENT"));
                                    jsonObject.put("MARKS", marks);
                                    jsonObject.put("TIME_TAKEN", getTimeFromMillis(Integer.parseInt(quiz.getTime_limit()) * 60000 - time_left));
                                    jsonObject.put("CORRECT", correct);
                                    jsonObject.put("WRONG", wrong);
                                    jsonArray.put(jsonObject);
                                    ParseObject result = new ParseObject("QuizResults");
                                    result.put("TITLE", quiz.getName());
                                    result.put("SUBJECT", quiz.getSubject());
                                    result.put("SEMESTER", quiz.getSem());
                                    result.put("BRANCH", quiz.getBranch());
                                    result.put("TOTAL_MARKS", Double.parseDouble(positive) * Double.parseDouble(quiz.getNo_of_ques()));
                                    result.put("RESULTS", jsonArray);
                                    result.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.d(TAG, "Result Saved");
                                            } else {
                                                Log.e(TAG, "ERROR", e);
                                            }
                                        }
                                    });

                                } else {
                                    Log.e(TAG, "ERROR", e);
                                }
                            }
                        } catch (Exception e1) {
                            Log.e(TAG, "ERROR", e1);
                        }
                    }
                });
                Log.d(TAG, "Marks:" + marks);
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onMarksCalculatedListener.onMarksCalculated(marks, correct, Double.parseDouble(positive) * Double.parseDouble(quiz.getNo_of_ques()));
                    }
                });
            }
        }.start();
    }

    protected static String getTimeFromMillis(long millis) {
        int sec = (int) (millis / 1000);
        int min = sec / 60;
        sec = sec % 60;
        String minS = min + "", secS = sec + "";
        if (min < 10) minS = "0" + minS;
        if (sec < 10) secS = "0" + secS;
        return (minS + ":" + secS);
    }

    public interface OnMarksCalculatedListener {
        void onMarksCalculated(double marks, int correct, double total);
    }
}
