package com.app.placefinderapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.item.ItemAbout;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.squareup.picasso.Picasso;

public class About_Activity  extends ActionBarActivity {

	Toolbar toolbar;
	ProgressBar pbar;
	TextView txttit,txtweb,txtemai,txtdesc;
	ImageView imglogo;
	List<ItemAbout> listabout;

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.about));
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		} 

		pbar=(ProgressBar)findViewById(R.id.progressBar1);
		txtdesc=(TextView)findViewById(R.id.textView_desc);
		txtemai=(TextView)findViewById(R.id.textView_emailad);
		txttit=(TextView)findViewById(R.id.textView_name);
		txtweb=(TextView)findViewById(R.id.textView_web);
		imglogo=(ImageView)findViewById(R.id.imageView_logo);
		listabout=new ArrayList<ItemAbout>();

		if (JsonUtils.isNetworkAvailable(About_Activity.this)) {
			new MyTask().execute(Constant.ABOUT_DETAILS_URL);

		} else {
			showToast(getString(R.string.conne_msg1));
			SetMessage();
		}
	}
	private	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pbar.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
 
			pbar.setVisibility(View.INVISIBLE);

			if (null == result || result.length() == 0) {
				showToast(getString(R.string.nodata));

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);

						ItemAbout objItem = new ItemAbout();
 
						objItem.setAppName(objJson.getString(Constant.ABOUT_NAME));
						objItem.setComDes(objJson.getString(Constant.ABOUT_DESC));
						objItem.setComLogo(objJson.getString(Constant.ABOUT_LOGO));
						objItem.setComEmail(objJson.getString(Constant.ABOUT_EMAIL));
						objItem.setComWebsite(objJson.getString(Constant.ABOUT_WEB));

						listabout.add(objItem);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setAdapterToListview();
			}

		}
	}

	public void setAdapterToListview() {

		ItemAbout about=listabout.get(0);
		txttit.setText(about.getAppName());
		txtdesc.setText(Html.fromHtml(about.getComDes()));
		txtemai.setText(about.getComEmail());
		txtweb.setText(about.getComWebsite());
 
		Picasso.with(About_Activity.this).load(Constant.SERVER_IMAGE_UPFOLDER+about.getComLogo().toString().replace(" ", "%20")).into(imglogo);

	}

	public void showToast(String msg) {
		Toast.makeText(About_Activity.this, msg, Toast.LENGTH_LONG).show();
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
	private AlertDialog SetMessage()
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(
				About_Activity.this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			alert = new AlertDialog.Builder(About_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			alert = new AlertDialog.Builder(About_Activity.this);
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


