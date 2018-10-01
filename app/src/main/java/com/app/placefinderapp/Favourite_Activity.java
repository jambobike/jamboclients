package com.app.placefinderapp;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.adapter.Favourite_Adapter;
import com.example.favorite.DatabaseHandler;
import com.example.favorite.Pojo;
import com.example.util.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Favourite_Activity extends ActionBarActivity{

	ListView grid_fav;
	DatabaseHandler db;
	Favourite_Adapter favo_adapter;
	List<Pojo> allData;
	Pojo pojoitem;
	int textlength = 0;
	String allArrayhid,allArrayhhotelname,allArrayhhotelimage,
	allArrayhhotelvideo,allArrayhhoteldes,allArrayhhotelmaplati,allArrayhhotelmaplongi,allArrayaddress,allArrayphone,allArrayemail,allArrayweb;	
	TextView txt_no;
	Toolbar toolbar;
	private AdView mAdView;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.favorite_activity);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.myfav));
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		} 
 
 		mAdView = (AdView) findViewById(R.id.adView);
		mAdView.loadAd(new AdRequest.Builder().build());

		db=new DatabaseHandler(getApplicationContext());
		grid_fav=(ListView)findViewById(R.id.subcategorylist_grid);
		txt_no=(TextView)findViewById(R.id.textView1);

		allData=db.getAllData();
		favo_adapter=new Favourite_Adapter(Favourite_Activity.this,R.layout.categorylist_item,allData);
		grid_fav.setAdapter(favo_adapter);

		if(allData.size()==0)
		{
			txt_no.setVisibility(View.VISIBLE);
		}
		else
		{
			txt_no.setVisibility(View.INVISIBLE);
		}

		grid_fav.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				pojoitem=allData.get(position);
				Constant.LISTING_H_IDD=pojoitem.H_id();
//				String pos=pojoitem.H_id();
//				String hid=pojoitem.H_id();
//				String hotelname=pojoitem.getHotel_name();
//				String hotelimage=pojoitem.getHotel_image();
//				String hotelvideo=pojoitem.getHotel_video();
//				String hoteldes=pojoitem.getHotel_description();
//				String hotellati=pojoitem.getHotel_map_latitude();
//				String hotellongi=pojoitem.getHotel_map_longitude();
//				String hoteladdress=pojoitem.getHotel_address();
//				String hotelemail=pojoitem.getHotel_eamil();
//				String hotelweb=pojoitem.getHotel_webb();
//				String hotelphone=pojoitem.getHotel_phone();

				Intent intplay=new Intent(getApplicationContext(),ListingDetails_Activity.class);
//				intplay.putExtra("POSITION", pos);
//				intplay.putExtra("HID", hid);
//				intplay.putExtra("HOTELNAME", hotelname);
//				intplay.putExtra("HOTELIMAGE", hotelimage);
//				intplay.putExtra("HOTELVIDEO", hotelvideo);
//				intplay.putExtra("HOTELDES", hoteldes);
//				intplay.putExtra("HOTELLATI", hotellati);
//				intplay.putExtra("HOTELLONGI", hotellongi);
//				intplay.putExtra("HOTELADDR", hoteladdress);
//				intplay.putExtra("HOTELEMAIL", hotelemail);
//				intplay.putExtra("HOTELWEB", hotelweb);
//				intplay.putExtra("HOTELPHONE", hotelphone);

				startActivity(intplay);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Log.e("Name", "Called");
		allData=db.getAllData();
		favo_adapter=new Favourite_Adapter(Favourite_Activity.this,R.layout.categorylist_item,allData);
		grid_fav.setAdapter(favo_adapter);

		if(allData.size()==0)
		{
			txt_no.setVisibility(View.VISIBLE);

		}
		else
		{
			txt_no.setVisibility(View.INVISIBLE);

		}

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
				favo_adapter.filter(text);

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
}
