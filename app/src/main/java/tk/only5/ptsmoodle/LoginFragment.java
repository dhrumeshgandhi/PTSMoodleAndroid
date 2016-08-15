package tk.only5.ptsmoodle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Activity activity;
    private String TAG = InitClass.TAG;
    private EditText etLoginEmail, etLoginPassword;
    private TextView tvLoginRegister;
    private Button btnLogin;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        activity = getActivity();
        activity.setTitle(getResources().getString(R.string.label_login));
        etLoginEmail = (EditText) rootView.findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) rootView.findViewById(R.id.etLoginPassword);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        tvLoginRegister = (TextView) rootView.findViewById(R.id.tvLoginRegister);
        btnLogin.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        Functions.hideSoftKeyboard(activity);
        if (view.equals(btnLogin)) {

            String email = etLoginEmail.getText().toString(), password = etLoginPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                dialog = Functions.showLoading(activity, "Logging");
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        dialog.dismiss();
                        if (e == null) {
                            Log.d(TAG, "Login Success");
                            FragmentManager fragmentManager = getFragmentManager();
                            Log.d(TAG, "popping backstack");
                            fragmentManager.popBackStack();
                            startActivity(new Intent(activity, UserFragmentActivity.class));
                            activity.finish();
                        } else {
                            Log.e(TAG, "Login Failed", e);
                            Snackbar.make(rootView, R.string.error_incorrect_credentials, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Log.e(TAG, "Not enough credentials!");
                Snackbar.make(rootView, R.string.error_insufficient_credentials, Snackbar.LENGTH_LONG).show();
            }
        } else if (view.equals(tvLoginRegister)) {
            Functions.setFragment(null, new RegisterFragment(), getResources().getString(R.string.label_register), R.id.fragmentContainerLoginSignUp, true);
        }
    }
}
