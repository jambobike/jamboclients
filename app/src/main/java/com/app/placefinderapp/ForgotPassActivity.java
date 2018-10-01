package com.app.placefinderapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

public class ForgotPassActivity extends ActionBarActivity implements ValidationListener{

	@Required(order = 1)
	@Email(order = 2, message = "Please Check and Enter a valid Email Address")
	EditText edtEmail;

	String strEmail,strMessage;
	private Validator validator;
	Button btncode;
	ProgressBar bar;
	LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);
		Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle("Forgot Password");
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		edtEmail=(EditText)findViewById(R.id.etUserName);
		btncode=(Button)findViewById(R.id.btnForgot);
		bar=(ProgressBar)findViewById(R.id.progressBar1);
		layout=(LinearLayout)findViewById(R.id.view);

		btncode.setOnClickListener(new View.OnClickListener() {

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
		strEmail=edtEmail.getText().toString();

		if (JsonUtils.isNetworkAvailable(ForgotPassActivity.this)) {
			new MyTaskForgot().execute(Constant.FORGET_PASSWORD_URL+strEmail);
		} else
		{
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

	private	class MyTaskForgot extends AsyncTask<String, Void, String> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			bar.setVisibility(View.VISIBLE);
			layout.setVisibility(View.INVISIBLE);
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
			layout.setVisibility(View.VISIBLE);
			edtEmail.setText("");
			edtEmail.requestFocus();
		}
		else
		{
			new SweetAlertDialog(ForgotPassActivity.this, SweetAlertDialog.SUCCESS_TYPE)
			.setTitleText("Success")
			.setContentText(strMessage)
			.setConfirmClickListener(new OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sweetAlertDialog) {
					// TODO Auto-generated method stub
					sweetAlertDialog.dismiss();
					Intent intentco=new Intent(ForgotPassActivity.this,SignInActivity.class);
					intentco.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentco);
					finish();
				}
			})
			.show();
		}
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

