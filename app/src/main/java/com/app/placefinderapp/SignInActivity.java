package com.app.placefinderapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class SignInActivity extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener, Validator.ValidationListener {

    CallbackManager callbackManager;
 //   LoginButton loginButton;
    String strEmail, strPassword, strMessage, strName, strPassengerId, strImage;
    @Required(order = 1)
    @Email(order = 2, message = "Please Check and Enter a valid Email Address")
    EditText edtEmail;

    @Required(order = 3)
    @Password(order = 4, message = "Enter a Valid Password")
    @TextRule(order = 5, minLength = 6, message = "Enter a Password Correctly")
    EditText edtPassword;
    private Validator validator;
    Button btnSingIn, btnSignUp, btnSignInG, btnSignInFb;
    MyApplication MyApp;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog mProgressDialog;
    Toolbar toolbar;
    TextView txtfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(SignInActivity.this);
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        this.setSupportActionBar(toolbar);

        MyApp = MyApplication.getInstance();
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnSingIn = (Button) findViewById(R.id.button);
        btnSignUp = (Button) findViewById(R.id.btn_create);
        btnSignInG = (Button) findViewById(R.id.btnSingInGoogle);
        btnSignInFb = (Button) findViewById(R.id.btnSignInFacebook);
        txtfor=(TextView)findViewById(R.id.textView_forget);
//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("public_profile", "email");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setScopes(gso.getScopeArray());

        btnSignInG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                MyApp.saveType("other");
            }
        });

        btnSignInFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile", "email"));
                MyApp.saveType("other");
            }
        });


//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                RequestData();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(SignInActivity.this, "Login attempt canceled.", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Toast.makeText(SignInActivity.this, "Login attempt failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validateAsync();
                MyApp.saveType("normal");

            }
        });

        txtfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ifor = new Intent(SignInActivity.this, ForgotPassActivity.class);
                ifor.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ifor);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
        initCallbackManager();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();
        if (JsonUtils.isNetworkAvailable(SignInActivity.this)) {
            new MyTaskLoginNormal().execute(Constant.NORMAL_LOGIN_URL + strEmail + "&password=" + strPassword);
         }
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, "Record Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void initCallbackManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                RequestData();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {


                        if (JsonUtils.isNetworkAvailable(SignInActivity.this)) {
                            strEmail = json.getString("email");
                            strName = json.getString("name").replace(" ", "%20");

                            new MyTaskLoginFacebook().execute(Constant.FACEBOOK_LOGIN_URL +strName+ "&email=" + strEmail+ "&phone==" + "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,gender,picture.width(150).height(150)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class MyTaskLoginFacebook extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has(Constant.MSG)) {
                            strMessage = objJson.getString(Constant.MSG);
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                        } else {
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                            strName = objJson.getString(Constant.USER_NAME);
                            strPassengerId = objJson.getString(Constant.USER_ID);
                            strImage = objJson.getString("other");

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

        }
    }
    private class MyTaskLoginNormal extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has(Constant.MSG)) {
                            strMessage = objJson.getString(Constant.MSG);
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                        } else {
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                            strName = objJson.getString(Constant.USER_NAME);
                            strPassengerId = objJson.getString(Constant.USER_ID);
                            strImage = objJson.getString("normal");

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

        }
    }
    private class MyTaskLoginGoogle extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {

            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        if (objJson.has(Constant.MSG)) {
                            strMessage = objJson.getString(Constant.MSG);
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                        } else {
                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
                            strName = objJson.getString(Constant.USER_NAME);
                            strPassengerId = objJson.getString(Constant.USER_ID);
                            strImage = objJson.getString("other");

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

        }
    }
    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            showToast("Opps. \n" + strMessage);

        } else {
            MyApp.saveIsLogin(true);
            MyApp.saveLogin(strPassengerId, strName, strEmail);
            Intent i = new Intent(SignInActivity.this, Category_Activity.class);
            startActivity(i);
            finish();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btnSingInGoogle).setVisibility(View.GONE);

        } else {
            findViewById(R.id.btnSingInGoogle).setVisibility(View.VISIBLE);

        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            // mStatusTextView.setText(acct.getDisplayName());
            // Log.e("URI", "" + acct.getPhotoUrl());
            updateUI(true);
            strName = acct.getDisplayName().toString().replace(" ", "%20");
            strEmail = acct.getEmail();
            if (JsonUtils.isNetworkAvailable(SignInActivity.this)) {
                new MyTaskLoginGoogle().execute(Constant.GMAIL_LOGIN_URL +strName + "&email=" + strEmail + "&phone=" + "");
            }
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
}
