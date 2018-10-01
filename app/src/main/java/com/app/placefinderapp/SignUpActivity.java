package com.app.placefinderapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NumberRule;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SignUpActivity extends ActionBarActivity implements Validator.ValidationListener{

    @Required(order = 1)
    @TextRule(order = 2, minLength = 3, maxLength = 35, trim = true, message = "Enter Valid Full Name")
    EditText edtFullName;

    @Required(order = 3)
    @Email(order = 4, message = "Please Check and Enter a valid Email Address")
    EditText edtEmail;

    @Required(order = 5)
    @Password(order = 6, message = "Enter a Valid Password")
    @TextRule(order = 7, minLength = 6, message = "Enter a Password Correctly")
    EditText edtPassword;

    @Required(order = 8)
    @NumberRule(order = 9, message = "Enter Phone Number in Numeric", type = NumberRule.NumberType.LONG)
    @TextRule(order = 10, message = "Enter valid Phone Number", minLength = 10, maxLength = 14)
    EditText edtMobile;


     private Validator validator;

    Button btnsignup;

    String strFullname,strId,strEmail,strPassword,strMessage,strMacAddress,strMobi,strCoun;

    ProgressBar bar;
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        toolbar.setTitle("Sign Up");
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtFullName=(EditText)findViewById(R.id.edt_user);
        edtEmail=(EditText)findViewById(R.id.edt_email);
        edtPassword=(EditText)findViewById(R.id.edt_password);
        edtMobile=(EditText)findViewById(R.id.edt_mobile);


        btnsignup=(Button)findViewById(R.id.button);
        bar=(ProgressBar)findViewById(R.id.progressBar1);
        scrollview=(ScrollView)findViewById(R.id.scrollView);


        btnsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                validator.validateAsync();
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        // TODO Auto-generated method stub
        strFullname=edtFullName.getText().toString().replace(" ", "%20");
        strEmail=edtEmail.getText().toString();
        strPassword=edtPassword.getText().toString();
        strMobi=edtMobile.getText().toString();


        if (JsonUtils.isNetworkAvailable(SignUpActivity.this)) {
            new MyTaskRegister().execute(Constant.REGISTER_URL+strFullname+"&email="+strEmail+"&password="+strPassword+"&phone="+strMobi+"&country="+strCoun);
         } else {
            setSweetDialog(SweetAlertDialog.ERROR_TYPE, getString(R.string.conne_msg1), getString(R.string.conne_msg2));
        }

    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        // TODO Auto-generated method stub
        String message = failedRule.getFailureMessage();
        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, "Record Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private	class MyTaskRegister extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
            scrollview.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            bar.setVisibility(View.GONE);

            if (null == result || result.length() == 0) {
                setSweetDialog(SweetAlertDialog.ERROR_TYPE, getString(R.string.conne_msg1), getString(R.string.nodata));
                showToast(getString(R.string.nodata));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        strMessage=objJson.getString(Constant.MSG);
                        Constant.GET_SUCCESS_MSG=objJson.getInt(Constant.SUCCESS);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult();
            }

        }
    }

    public void setResult() {

        if(Constant.GET_SUCCESS_MSG==0)
        {
            setSweetDialog(SweetAlertDialog.ERROR_TYPE, "Opps.", strMessage);
            scrollview.setVisibility(View.VISIBLE);
            edtEmail.setText("");
            edtEmail.requestFocus();
        }
        else
        {
            new SweetAlertDialog(SignUpActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Thanks for Register")
                    .setContentText(strMessage)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // TODO Auto-generated method stub
                            sweetAlertDialog.dismiss();
                            Intent int1=new Intent(getApplicationContext(),SignInActivity.class);
                            int1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(int1);
                        }
                    })
                    .show();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void setSweetDialog(int code,String title,String message)
    {
        new SweetAlertDialog(this,code)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}

