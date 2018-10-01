package com.app.placefinderapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.util.Constant;
import com.example.util.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Profile_Activity extends ActionBarActivity {

	EditText txt_email,txt_name,txt_pwd,txt_phone;
	MyApplication App;
	Button btn_update;
	String strMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle("Profile");
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		App = MyApplication.getInstance();

		txt_email=(EditText)findViewById(R.id.edt_email);
		txt_name=(EditText)findViewById(R.id.edt_user);
		txt_phone=(EditText)findViewById(R.id.edt_mobile);
		txt_pwd=(EditText)findViewById(R.id.edt_password);
		btn_update=(Button)findViewById(R.id.button);

		if (JsonUtils.isNetworkAvailable(Profile_Activity.this)) {
			new MyTask().execute(Constant.PROFILE_URL + App.getUserId());

		} else {
			showToast(getString(R.string.conne_msg1));
			SetMessage();
		}


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
	private	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Profile_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
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
				showToast(getString(R.string.nodata));

			} else {

				try {

					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);

						String name = objJson.getString("name");
						String email = objJson.getString("email");
						String phone = objJson.getString("phone");
						txt_name.setText(name);
						txt_email.setText(email);
						txt_phone.setText(phone);
						txt_pwd.setText("123456");

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setAdapter();
			}
		}
	}

	public void setAdapter() {

		btn_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (JsonUtils.isNetworkAvailable(Profile_Activity.this)) {
					new MyTaskUp().execute(Constant.PROFILE_UPDATE_URL + App.getUserId()+"&name="+txt_name.getText().toString().replace(" ", "%20")+"&email="+txt_email.getText().toString()+"&password="+txt_pwd.getText().toString()+"&phone="+txt_phone.getText().toString());

				} else {
					showToast(getString(R.string.conne_msg1));
					SetMessage();
				}
			}
		});
	}


	public void showToast(String msg) {
		Toast.makeText(Profile_Activity.this, msg, Toast.LENGTH_LONG).show();
	}
	private	class MyTaskUp extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Profile_Activity.this, AlertDialog.THEME_HOLO_LIGHT);
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

		}
		else
		{
			Toast.makeText(Profile_Activity.this,"Your Profile Updated",Toast.LENGTH_SHORT).show();
			Intent intentco=new Intent(Profile_Activity.this,Category_Activity.class);
			intentco.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentco);
 		}
	}
	private AlertDialog SetMessage()
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(
				Profile_Activity.this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			alert = new AlertDialog.Builder(Profile_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			alert = new AlertDialog.Builder(Profile_Activity.this);
		}
		alert.setTitle(R.string.conne_msg1);
		alert.setIcon(R.drawable.app_icon);
		alert.setMessage(getString(R.string.conne_msg2));

		alert.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int whichButton) {

						finish();
					}

				});
		alert.show();
		return alert.create();
	}

}
