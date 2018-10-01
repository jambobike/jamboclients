package com.app.placefinderapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adapter.Category_Adapter;
import com.example.item.Item_Category;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.example.util.PopUpAds;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class Category_Activity extends ActionBarActivity implements  GoogleApiClient.OnConnectionFailedListener {

	GridView gridView;
	ArrayList<Item_Category> arrayOfCategory ;
	Category_Adapter categorydAdapter;
	Item_Category item;
	Toolbar toolbar;
	ProgressBar pbar;
	private AdView mAdView;
 	MyApplication App;
	private GoogleApiClient mGoogleApiClient;
	CallbackManager callbackManager;
	private InterstitialAd mInterstitial;

 	@Override
	protected void onCreate(Bundle savedInstanceState) {
		callbackManager = CallbackManager.Factory.create();
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.category_activity);
		FacebookSdk.sdkInitialize(this);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.app_name));
		this.setSupportActionBar(toolbar);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		}
		gridView = (GridView) findViewById(R.id.category_gridview);
		arrayOfCategory= new ArrayList<Item_Category>();
		pbar=(ProgressBar)findViewById(R.id.progressBar1);
		App = MyApplication.getInstance();

		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.loadAd(new AdRequest.Builder().build());


		try {
			PackageInfo info = getPackageManager().getPackageInfo("com.app.placefinderapp", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

			}
		} catch (PackageManager.NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {
 		}

		if (JsonUtils.isNetworkAvailable(Category_Activity.this)) {
			new MyTask().execute(Constant.CATEGORY_URL);
		} else {
			showToast(getString(R.string.conne_msg1));
			SetMessage();
		}
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestProfile()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				item=arrayOfCategory.get(position);
				Constant.CATEGORYID=item.getCategoryId();
				Constant.CATEGORYNAME=item.getCategoryName();
				PopUpAds.ShowInterstitialAds(Category_Activity.this);
				Intent intentsub=new Intent(getApplicationContext(),Listing_Activity.class);
				intentsub.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentsub);

			}
		});

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	public void signOut() {
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
					}
				});
	}
	private	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pbar.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			pbar.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);

			if (null == result || result.length() == 0) {
				showToast(getString(R.string.nodata));

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);

						Item_Category objItem = new Item_Category();

						objItem.setCategoryId(objJson.getString(Constant.CATEGORY_CID));
						objItem.setCategoryName(objJson.getString(Constant.CATEGORY_NAME));
						objItem.setImageurl(objJson.getString(Constant.CATEGORY_IMAGE));

						arrayOfCategory.add(objItem);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setAdapterToListview();
			}

		}
	}
	public void setAdapterToListview() {
		categorydAdapter = new Category_Adapter(Category_Activity.this, R.layout.category_item,
				arrayOfCategory);
		gridView.setAdapter(categorydAdapter);
	}

	public void showToast(String msg) {
		Toast.makeText(Category_Activity.this, msg, Toast.LENGTH_LONG).show();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case R.id.menu_fav:
			Intent intentfa=new Intent(getApplicationContext(),Favourite_Activity.class);
			intentfa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentfa);
			return true;

		case R.id.menu_about:
			Intent intentabout=new Intent(getApplicationContext(),About_Activity.class);
			intentabout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentabout);

			return true;
			case R.id.menu_profilep:
				if(App.getType().equals("normal"))
				{
					Intent intentpr=new Intent(getApplicationContext(),Profile_Activity.class);
					intentpr.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentpr);
 				}
				else
				{
					Intent intentprfb=new Intent(getApplicationContext(),ProfileFb_Activity.class);
					intentprfb.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentprfb);
 				}
				return true;
			case R.id.menu_logout:
				LoginManager.getInstance().logOut();
				signOut();
				MyApplication.getInstance().saveIsLogin(false);
				finish();
				return true;
		}

		return(super.onOptionsItemSelected(item));
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Toast.makeText(appContext, "BAck", Toast.LENGTH_LONG).show();
			AlertDialog.Builder alert = new AlertDialog.Builder(
					Category_Activity.this);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				alert = new AlertDialog.Builder(Category_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
			} else {
				alert = new AlertDialog.Builder(Category_Activity.this);
			}
			alert.setTitle(R.string.app_name);
			alert.setIcon(R.drawable.app_icon);
			alert.setMessage("Are You Sure You Want To Quit?");

			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {

					finish();
				}

			});

			alert.setNegativeButton("Rate App",
					new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					final String appName = getPackageName();
					try {
						startActivity(new Intent(Intent.ACTION_VIEW,
								Uri.parse("market://details?id="
										+ appName)));
					} catch (android.content.ActivityNotFoundException anfe) {
						startActivity(new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://play.google.com/store/apps/details?id="
										+ appName)));
					}
				}
			});
			alert.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	private AlertDialog SetMessage()
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(
				Category_Activity.this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			alert = new AlertDialog.Builder(Category_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			alert = new AlertDialog.Builder(Category_Activity.this);
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
