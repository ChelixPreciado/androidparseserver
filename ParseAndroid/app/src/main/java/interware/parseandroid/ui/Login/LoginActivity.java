package interware.parseandroid.ui.Login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import interware.parseandroid.R;
import interware.parseandroid.models.User;
import interware.parseandroid.parseserver.UsersHandler;
import interware.parseandroid.persistencia.UserSession;
import interware.parseandroid.ui.ParseappActivity;
import interware.parseandroid.ui.PostLists.PostListsActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ParseappActivity implements OnClickListener {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validateLogin();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (isValidData()){
                        User u = new User(mEmailView.getText().toString(), mPasswordView.getText().toString(),
                                "Email");
                        attemptLogin(u);
                    }
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
        Button mEmailSignUpButton = (Button)findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
    }


    private boolean isValidData(){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        return !cancel;
    }

    private void attemptLogin(final User user) {
        showLoader(true);
        UsersHandler.logInUser(user, new UsersHandler.logInUserCallback() {
            @Override
            public void isLogguedIn(boolean loggedIn) {
                showLoader(false);
                if (loggedIn) {
                    UserSession userSession = new UserSession(getApplicationContext());
                    userSession.saveUserName(user.getUsername());
                    startActivity(new Intent(LoginActivity.this, PostListsActivity.class));
                    finish();
                }else
                    Log.i("Chelix", "Usuario invalido");
            }

            @Override
            public void onError(String errorMsg) {
                Log.i("Chelix", "Error en log in: " + errorMsg);
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptRegister(final User user){
        showLoader(true);
        UsersHandler.registerUser(user, new UsersHandler.registerUserCallback() {
            @Override
            public void isRegistered(boolean registered) {
                Log.i("Chelix", "Usuario registrado.. :)");
                UserSession userSession = new UserSession(getApplicationContext());
                userSession.saveUserName(user.getUsername());
                showLoader(false);
                startActivity(new Intent(LoginActivity.this, PostListsActivity.class));
                finish();
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.email_sign_in_button:
                if (isValidData()){
                    User u = new User(mEmailView.getText().toString(), mPasswordView.getText().toString(),
                            "Email");
                    attemptLogin(u);
                }
                break;
            case R.id.email_sign_up_button:
                if (isValidData()){
                    User u = new User(mEmailView.getText().toString(), mPasswordView.getText().toString(),
                            "Email");
                    attemptRegister(u);
                }
                break;
        }
    }

    private void validateLogin(){
        UserSession userSession = new UserSession(getApplicationContext());
        if (userSession.isLoggedIn()){
            startActivity(new Intent(this, PostListsActivity.class));
            finish();
        }
    }
}

