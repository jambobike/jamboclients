package com.app.placefinderapp;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adapter.Listing_Adapter;
import com.example.item.Item_Listing;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.example.util.PopUpAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Listing_Activity  extends ActionBarActivity
{
	ListView gridView;
	ArrayList<Item_Listing> arrayOfListing ;
	Listing_Adapter listingadpter;
 	Item_Listing itemslisting;
	int textlength = 0;
	private Item_Listing objAllBeanlisting;
	private ArrayList<Item_Listing> arraylisting;
	ArrayList<String> allListsubsubcatId,allListsubsubcatName;
	String[] allArraysubsubcatId,allArraysubsubcatName;
	Toolbar toolbar;
	ProgressBar pbar;
	private AdView mAdView;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.categorylist_activity);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(Constant.CATEGORYNAME);
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		} 
		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.loadAd(new AdRequest.Builder().build());

		pbar=(ProgressBar)findViewById(R.id.progressBar1);
		gridView = (ListView)findViewById(R.id.subcategorylist_grid);
		arrayOfListing= new ArrayList<Item_Listing>();
		this.arraylisting = new ArrayList<Item_Listing>();

		if (JsonUtils.isNetworkAvailable(Listing_Activity.this)) {
			new MyTask().execute(Constant.LISTING_URL+Constant.CATEGORYID);
			 
		} else {
			showToast(getString(R.string.conne_msg1));
			SetMessage();
		}

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub

				objAllBeanlisting=arrayOfListing.get(position);
				Constant.LISTING_H_IDD=objAllBeanlisting.getH_id();
				PopUpAds.ShowInterstitialAds(Listing_Activity.this);
				Intent intplay=new Intent(getApplicationContext(),ListingDetails_Activity.class);
 				startActivity(intplay);
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

						Item_Listing objItem = new Item_Listing();

						objItem.setH_id(objJson.getString(Constant.LISTING_H_ID));
						objItem.setHotel_name(objJson.getString(Constant.LISTING_H_NAME));
						objItem.setHotel_image(objJson.getString(Constant.LISTING_H_IMAGE));
						objItem.setHotel_video(objJson.getString(Constant.LISTING_H_VIDEO));
						objItem.setHotel_description(objJson.getString(Constant.LISTING_H_DES));
						objItem.setHotel_map_latitude(objJson.getString(Constant.LISTING_H_MAP_LATI));
						objItem.setHotel_map_longitude(objJson.getString(Constant.LISTING_H_MAP_LONGI));
						objItem.setHotel_Address(objJson.getString(Constant.LISTING_H_ADDRESS));
						objItem.setHotel_email(objJson.getString(Constant.LISTING_H_EMAIL));
						objItem.setHotel_phone(objJson.getString(Constant.LISTING_H_PHONE));
						objItem.setHotel_website(objJson.getString(Constant.LISTING_H_WEBSITE));
						objItem.setHotel_rating(objJson.getString(Constant.LISTING_H_RATING));

						arrayOfListing.add(objItem);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				arraylisting.addAll(arrayOfListing);
				setAdapterToListview();
			}

		}
	}
	public void setAdapterToListview() {
		listingadpter = new Listing_Adapter(Listing_Activity.this, R.layout.categorylist_item,
				arrayOfListing);
		gridView.setAdapter(listingadpter);
	}

	public void showToast(String msg) {
		Toast.makeText(Listing_Activity.this, msg, Toast.LENGTH_LONG).show();
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		arrayOfListing.clear();
		if (charText.length() == 0) {
			arrayOfListing.addAll(arraylisting);
		} 
		else 
		{
			for (Item_Listing wp : arraylisting) 
			{
				if (wp.getHotel_name().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					arrayOfListing.add(wp);
				}
			}
		}

		setAdapterToListview();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_search, menu);

		final SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();

		final MenuItem searchMenuItem = menu.findItem(R.id.search);
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

			@SuppressLint("NewApi") @Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus) {
					searchMenuItem.collapseActionView();
					searchView.setQuery("", false);
				}
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {


				String text = newText.toString().toLowerCase(Locale.getDefault());
				filter(text);
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// Do something
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
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
				Listing_Activity.this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			alert = new AlertDialog.Builder(Listing_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			alert = new AlertDialog.Builder(Listing_Activity.this);
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
