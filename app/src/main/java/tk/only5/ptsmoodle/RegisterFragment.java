package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private EditText etRegisterEmail, etRegisterPassword, etRegisterConfirmPassword, etRegisterFName, etRegisterLName, etRegisterMobile, etRegisterStudentName, etRegisterStudentEnroll, etRegisterTeacherId;
    private RadioGroup rgExtraDetails;
    private FrameLayout fragementContainerExtraDetails;
    private RadioButton radioButton = null;
    private ExtraDetailsRegisterFragment extraDetailsRegisterFragment;
    private Bundle bundle;
    private Button btnRegister;
    private String currentPost = "";
    private ListView lvTeacherExtraDetailsAddClassSubject;
    private Spinner spinStudentSem, spinStudentBranch;
    private ParseUser user = null;
    private ProgressDialog dialog;
    private View[] views = null;
    private int errorCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        activity = getActivity();
        activity.setTitle(getResources().getString(R.string.label_register));
        etRegisterEmail = (EditText) rootView.findViewById(R.id.etRegisterEmail);
        etRegisterPassword = (EditText) rootView.findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = (EditText) rootView.findViewById(R.id.etRegisterConfirmPassword);
        etRegisterFName = (EditText) rootView.findViewById(R.id.etRegisterFName);
        etRegisterLName = (EditText) rootView.findViewById(R.id.etRegisterLName);
        etRegisterMobile = (EditText) rootView.findViewById(R.id.etRegisterMobile);
        rgExtraDetails = (RadioGroup) rootView.findViewById(R.id.rgExtraDetails);
        rgExtraDetails.setOnCheckedChangeListener(this);
        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        fragementContainerExtraDetails = (FrameLayout) rootView.findViewById(R.id.fragmentContainerExtraDetails);

        etRegisterEmail.addTextChangedListener(new InputValidator(etRegisterEmail));
        etRegisterPassword.addTextChangedListener(new InputValidator(etRegisterPassword));
        etRegisterMobile.addTextChangedListener(new InputValidator(etRegisterMobile));

        etRegisterEmail.setOnEditorActionListener(new EmptyTextListener(etRegisterEmail));
        etRegisterPassword.setOnEditorActionListener(new EmptyTextListener(etRegisterPassword));
        etRegisterConfirmPassword.setOnEditorActionListener(new EmptyTextListener(etRegisterConfirmPassword));
        etRegisterFName.setOnEditorActionListener(new EmptyTextListener(etRegisterFName));
        etRegisterLName.setOnEditorActionListener(new EmptyTextListener(etRegisterLName));
        etRegisterMobile.setOnEditorActionListener(new EmptyTextListener(etRegisterMobile));

        return rootView;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup.equals(rgExtraDetails)) {
            radioButton = (RadioButton) radioGroup.findViewById(i);
            currentPost = radioButton.getHint().toString();
            bundle = new Bundle();
            bundle.putString(getResources().getString(R.string.tag_extra_details), currentPost);
            extraDetailsRegisterFragment = new ExtraDetailsRegisterFragment();
            extraDetailsRegisterFragment.setArguments(bundle);
            Functions.setFragment(null, extraDetailsRegisterFragment, "EXTRA_DETAILS_" + currentPost, R.id.fragmentContainerExtraDetails, false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnRegister)) {
            user = new ParseUser();
            String email, password, cpassword, fname, lname, mobile;
            email = etRegisterEmail.getText().toString();
            password = etRegisterPassword.getText().toString();
            cpassword = etRegisterConfirmPassword.getText().toString();
            fname = etRegisterFName.getText().toString();
            lname = etRegisterLName.getText().toString();
            mobile = etRegisterMobile.getText().toString();
            if (!email.isEmpty() && !password.isEmpty() && !cpassword.isEmpty()
                    && !fname.isEmpty() && !lname.isEmpty() && !mobile.isEmpty()
                    && radioButton != null
                    && Pattern.matches("[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}", email)
                    && Pattern.matches("[0-9]{10}", mobile)) {
                if (password.equals(cpassword)) {
                    dialog = Functions.showLoading(activity, "Signing Up");
                    user.setEmail(email);
                    user.setUsername(email);
                    user.setPassword(password);
                    user.put("FIRST_NAME", fname);
                    user.put("LAST_NAME", lname);
                    user.put("MOBILE", mobile);
                    user.put("POST", currentPost);
                    views = Functions.getChildFragmentViews();
                    switch (currentPost) {
                        case "Teacher":
                            lvTeacherExtraDetailsAddClassSubject = (NonScrollListView) views[1];
                            etRegisterTeacherId = (EditText) views[0];
                            final String teacherID = etRegisterTeacherId.getText().toString();
                            final JSONArray classSubjectPairs = ((AddClassSubjectListAdapter) lvTeacherExtraDetailsAddClassSubject.getAdapter()).getClassSubjectPairs();
                            if (!teacherID.isEmpty() && classSubjectPairs.length() > 0) {
                                user.put("TEACHER_ID", teacherID);
                                user.put("CLASS_SUBJECT", classSubjectPairs);
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        dialog.dismiss();
                                        if (e == null) {
                                            Log.d(TAG, "SignUp Success");
                                            startActivity(new Intent(activity, UserFragmentActivity.class));
                                        } else {
                                            Log.e(TAG, "SignUp Failed", e);
                                            Snackbar.make(rootView, R.string.error_technical, Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Log.e(TAG, "All details are not filled!");
                                Snackbar.make(rootView, R.string.error_fill_details, Snackbar.LENGTH_LONG).show();
                            }
                            break;
                        case "Student":
                            final String enroll, branch, sem;
                            etRegisterStudentEnroll = (EditText) views[0];
                            spinStudentSem = (Spinner) views[1];
                            spinStudentBranch = (Spinner) views[2];
                            enroll = etRegisterStudentEnroll.getText().toString();
                            branch = (String) spinStudentBranch.getSelectedItem();
                            sem = (int) spinStudentSem.getSelectedItem() + "";
                            if (!enroll.isEmpty() && !branch.isEmpty() && !sem.isEmpty()) {
                                user.put("ENROLLMENT", enroll);
                                user.put("SEMESTER", sem);
                                user.put("BRANCH", branch);
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        dialog.dismiss();
                                        if (e == null) {
                                            Log.d(TAG, "SignUp Success");
                                            startActivity(new Intent(activity, UserFragmentActivity.class));
                                        } else {
                                            Log.e(TAG, "SignUp Failed", e);
                                            Snackbar.make(rootView, R.string.error_technical, Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Log.e(TAG, "All details are not filled!");
                                Snackbar.make(rootView, R.string.error_fill_details, Snackbar.LENGTH_LONG).show();
                            }
                            break;
                        case "Parent":
                            final String studentName, studentEnroll;
                            etRegisterStudentName = (EditText) views[0];
                            etRegisterStudentEnroll = (EditText) views[1];
                            studentName = etRegisterStudentName.getText().toString();
                            studentEnroll = etRegisterStudentEnroll.getText().toString();
                            if (!studentEnroll.isEmpty() && !studentName.isEmpty()) {
                                user.put("STUDENT_NAME", studentName);
                                user.put("STUDENT_ENROLL", studentEnroll);
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        dialog.dismiss();
                                        if (e == null) {
                                            Log.d(TAG, "SignUp Success");
                                            startActivity(new Intent(activity, UserFragmentActivity.class));
                                        } else {
                                            Log.e(TAG, "SignUp Failed", e);
                                            Snackbar.make(rootView, R.string.error_technical, Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Log.e(TAG, "All details are not filled!");
                                Snackbar.make(rootView, R.string.error_fill_details, Snackbar.LENGTH_LONG).show();
                            }
                            break;
                    }

                    FragmentManager fragmentManager = getFragmentManager();
                    Log.d(TAG, "popping backstack");
                    fragmentManager.popBackStack();
                    activity.finish();
                } else {
                    Log.e(TAG, "Passwords don't match!");
                    Snackbar.make(rootView, R.string.error_password_not_match, Snackbar.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "All details are not filled!");
                Snackbar.make(rootView, R.string.error_fill_details, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    protected class InputValidator implements TextWatcher {
        private EditText editText;

        public InputValidator(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (charSequence.length() != 0) {
                switch (editText.getId()) {
                    case R.id.etRegisterEmail:
                        if (!Pattern.matches("[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}", charSequence)) {
                            editText.setError("Invalid Email!");
                        }
                        break;
                    case R.id.etRegisterPassword:
                        break;
                    case R.id.etRegisterMobile:
                        if (!Pattern.matches("[0-9]{10}", charSequence)) {
                            editText.setError("Invalid Mobile!");
                        }
                        break;
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }
    }

    protected class EmptyTextListener implements EditText.OnEditorActionListener {
        private EditText editText;

        public EmptyTextListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("This Field is Required!");
                }
            }
            return false;
        }
    }

}
