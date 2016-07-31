package com.jinchao.express.activity;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.widget.NavigationLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity{

    @ViewInject(R.id.navigation)
    private NavigationLayout navigationLayout;
    @ViewInject(R.id.email) private AutoCompleteTextView mEmailView;
    @ViewInject(R.id.password) private EditText mPasswordView;
//    @ViewInject(R.id.login_form) private View mLoginFormView;
    @ViewInject(R.id.btn_log) private  Button mEmailSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the login form.
        navigationLayout.setCenterText(getResources().getString(R.string.title_activity_login));
        navigationLayout.setLeftTextOnClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }




    private void attemptLogin() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        View focusView = null;
        // Check for a valid mobile.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            focusView.requestFocus();
//            return;
//        } else if (!CommonUtils.isMobile(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            focusView.requestFocus();
//            return;
//        }
//        if (!TextUtils.isEmpty(password)&&!CommonUtils.isPassword(password)){
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            focusView.requestFocus();
//            return;
//        }

        loginRequest(email,password);
    }

    private void loginRequest(String phone,String password){
        Intent intent =new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}

