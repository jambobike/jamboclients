package com.app.placefinderapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {

	private GoogleMap googleMap;
	int position;
	String alllatitude,alllongitude,allplacename;
	Toolbar toolbar;
	LatLng TutorialsPoint;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.app_name));
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		} 

		Intent i=getIntent();
		position=i.getIntExtra("POSITION", 0);

		alllatitude=i.getStringExtra("LATITUDE");
		alllongitude=i.getStringExtra("LONGITUDE");
		allplacename=i.getStringExtra("PLACENAME");
		 TutorialsPoint = new LatLng(Double.parseDouble(alllatitude), Double.parseDouble(alllongitude));
		try {
			if (googleMap == null) {
				//googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync();
				MapFragment mapFragment = (MapFragment) getFragmentManager()
						.findFragmentById(R.id.map);
				mapFragment.getMapAsync(this);
			}


		} catch (Exception e) {
			e.printStackTrace();
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

	@Override
	public void onMapReady(GoogleMap googleMap) {
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TutorialsPoint, 15.0f));
		googleMap.addMarker(new MarkerOptions().position(TutorialsPoint)
				.title(allplacename));
	}
}