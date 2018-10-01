package com.app.placefinderapp;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.favorite.DatabaseHandler;
import com.example.favorite.Pojo;
import com.example.item.Item_Listing;
import com.example.util.Constant;
import com.example.util.JsonUtils;
import com.example.youtube.YoutubePlay;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListingDetails_Activity  extends ActionBarActivity{


 	public DatabaseHandler db;
	String allArrayhid,allArrayhhotelname,allArrayhhotelimage,
	allArrayhhotelvideo,allArrayhhoteldes,allArrayhhotelmaplati,allArrayhhotelmaplongi,allArrayaddress,allArrayphone,allArrayemail,allArrayweb,allArrayrate;
	int position;
	String[] allArrayimages;
	ArrayList<String> allListimages;
	ImageView imgfavourite,imgplace,imgvideoplay,imgmap,imgshare,imgnext,imgprev;
 	Toolbar toolbar;
	TextView txt_placetitle,txtadd,txtphone,txtweb,txteamil;
	private AdView mAdView;
	WebView txt_placedes;
	ArrayList<Item_Listing> arrayOfcatalogdetail ;
	Item_Listing itemcatelist;
	private ArrayList<Item_Listing> arraylist;
	ViewPager view_img;
	int TOTAL_IMAGE;
	String rate_msg;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
  		setContentView(R.layout.listingdetails_activity);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle(getString(R.string.app_name));
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
		Constant.DEVICE_ID= Settings.Secure.getString(ListingDetails_Activity.this.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		imgplace=(ImageView)findViewById(R.id.image_place);
		txt_placetitle=(TextView)findViewById(R.id.text_palcetitle);
		txt_placedes=(WebView)findViewById(R.id.text_description);
		imgvideoplay=(ImageView)findViewById(R.id.image_videoplay);
		imgmap=(ImageView)findViewById(R.id.image_map);
		imgshare=(ImageView)findViewById(R.id.image_share);
		imgfavourite=(ImageView)findViewById(R.id.image_fav);
		txtadd=(TextView)findViewById(R.id.text_address);
		txteamil=(TextView)findViewById(R.id.text_email);
		txtphone=(TextView)findViewById(R.id.text_phone);
		txtweb=(TextView)findViewById(R.id.text_web);
 		imgnext=(ImageView)findViewById(R.id.imageView4);
		imgprev=(ImageView)findViewById(R.id.imageView3);
		view_img=(ViewPager)findViewById(R.id.slider);

		arrayOfcatalogdetail= new ArrayList<Item_Listing>();
		this.arraylist = new ArrayList<Item_Listing>();

		allListimages=new ArrayList<String>();

		allArrayimages=new String[allListimages.size()];

		view_img.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				//position=viewcatalogimg.getCurrentItem();

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		if (JsonUtils.isNetworkAvailable(ListingDetails_Activity.this)) {
			new MyTask().execute(Constant.LISTING_DETAILS_URL + Constant.LISTING_H_IDD);

		} else {
			showToast(getString(R.string.conne_msg1));
			SetMessage();
		}

		imgnext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				position = view_img.getCurrentItem();
				position++;
				if (position == TOTAL_IMAGE) {
					position = TOTAL_IMAGE;
				}
				view_img.setCurrentItem(position);
			}
		});

		imgprev.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				position = view_img.getCurrentItem();
				position--;
				if (position < 0) {
					position = 0;
				}
				view_img.setCurrentItem(position);
			}
		});
	}

	private	class MyTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);


			if (null == result || result.length() == 0) {
				showToast("No data found!!");
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

						arrayOfcatalogdetail.add(objItem);

						JSONArray jsonArraychild = objJson.getJSONArray("gallery");

						if(jsonArraychild.length()==0)
						{


						}
						else
						{
							for(int j=0;j<jsonArraychild.length();j++)
							{

								JSONObject thumbnail = jsonArraychild.getJSONObject(j);
								allListimages.add(thumbnail.getString("image_name"));

								allArrayimages=allListimages.toArray(allArrayimages);

							}
						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				arraylist.addAll(arrayOfcatalogdetail);

			}
			setAdapterToListview();
		}
		public void setAdapterToListview() {


			itemcatelist=arrayOfcatalogdetail.get(0);
// 			Picasso.with(ListingDetails_Activity.this).load(Constant.SERVER_IMAGE_UPFOLDER+itemcatelist.getHotel_image().toString().replace(" ", "%20")).into(imgplace);
			txt_placetitle.setText(itemcatelist.getHotel_name().toString());
			//String formattedString=android.text.Html.fromHtml(allArrayhhoteldes).toString();
			//txt_placedes.setText(Html.fromHtml(itemcatelist.getHotel_description().toString()));
			txt_placedes.setBackgroundColor(0);
			txt_placedes.setFocusableInTouchMode(false);
			txt_placedes.setFocusable(false);
			txt_placedes.getSettings().setDefaultTextEncodingName("UTF-8");
			txt_placedes.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			String mimeType = "text/html;charset=UTF-8";
			String encoding = "utf-8";
			String htmlText = itemcatelist.getHotel_description();

			String text = "<html><head>"
					+ "<style type=\"text/css\">body{color: #545454;text-align:justify}"
					+ "</style></head>"
					+ "<body>"
					+  htmlText
					+ "</body></html>";

			txt_placedes.loadData(text, mimeType, encoding);
			txtadd.setText(itemcatelist.getHotel_Address().toString());
			txtphone.setText(itemcatelist.getHotel_phone().toString());
			txteamil.setText(itemcatelist.getHotel_email().toString());
			txtweb.setText(itemcatelist.getHotel_website().toString());
			ImagePagerAdapter adapter = new ImagePagerAdapter();
			view_img.setAdapter(adapter);

			List<Pojo> pojolist = db.getFavRow(itemcatelist.getH_id().toString());
			if (pojolist.size() == 0) {

				imgfavourite.setImageResource(R.drawable.ic_favourite);
			} else {
				if (pojolist.get(0).H_id().equals(itemcatelist.getH_id().toString())) ;
				{
					imgfavourite.setImageResource(R.drawable.ic_favourite_hover);
				}

			}

			imgvideoplay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (TextUtils.isEmpty(itemcatelist.getHotel_video().toString())) {
						Toast.makeText(ListingDetails_Activity.this, "No Video For Playing", Toast.LENGTH_SHORT).show();
					} else {
						Intent i = new Intent(ListingDetails_Activity.this, YoutubePlay.class);
						i.putExtra("id", itemcatelist.getHotel_video().toString());
						startActivity(i);
					}
				}
			});



			imgmap.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intentmap = new Intent(getApplicationContext(), MapActivity.class);
					intentmap.putExtra("LATITUDE", itemcatelist.getHotel_map_latitude().toString());
					intentmap.putExtra("LONGITUDE", itemcatelist.getHotel_map_longitude().toString());
					intentmap.putExtra("PLACENAME", itemcatelist.getHotel_name().toString());
					intentmap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentmap);
				}
			});




			imgfavourite.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					allArrayhid = itemcatelist.getH_id().toString();
					allArrayhhotelname = itemcatelist.getHotel_name().toString();
					allArrayhhotelimage = itemcatelist.getHotel_image().toString();
					allArrayhhotelvideo = itemcatelist.getHotel_video().toString();
					allArrayhhoteldes = itemcatelist.getHotel_description().toString();
					allArrayhhotelmaplati = itemcatelist.getHotel_map_latitude().toString();
					allArrayhhotelmaplongi = itemcatelist.getHotel_map_longitude().toString();
					allArrayaddress = itemcatelist.getHotel_Address().toString();
					allArrayemail = itemcatelist.getHotel_email().toString();
					allArrayphone = itemcatelist.getHotel_email().toString();
					allArrayweb = itemcatelist.getHotel_website().toString();
					allArrayrate=itemcatelist.getHotel_rating().toString();

					List<Pojo> pojolist = db.getFavRow(allArrayhid);
					if (pojolist.size() == 0) {

						Toast.makeText(getApplicationContext(), "Add to Favorite", Toast.LENGTH_SHORT).show();
						db.AddtoFavorite(new Pojo(allArrayhid, allArrayhhotelname, allArrayhhotelimage,
								allArrayhhotelvideo, allArrayhhoteldes, allArrayhhotelmaplati, allArrayhhotelmaplongi, allArrayaddress,
								allArrayemail, allArrayphone, allArrayweb, allArrayrate));
						imgfavourite.setImageResource(R.drawable.ic_favourite_hover);
					} else {
						if (pojolist.get(0).H_id().equals(itemcatelist.getH_id().toString())) {

							db.RemoveFav(new Pojo(itemcatelist.getH_id().toString()));
							Toast.makeText(getApplicationContext(), "Removed From Favorite", Toast.LENGTH_SHORT).show();
							imgfavourite.setImageResource(R.drawable.ic_favourite);
						}
					}
				}
			});

			imgshare.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
 					allArrayhhotelname = itemcatelist.getHotel_name().toString();
 					allArrayaddress = itemcatelist.getHotel_Address().toString();
					Intent sendIntent = new Intent();
					sendIntent.setAction(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_TEXT, allArrayhhotelname+"\n"+allArrayaddress+"\n"+"Download This Application from PlayStore " + "https://play.google.com/store/apps/details?id=" + getPackageName());
					sendIntent.setType("text/plain");
					startActivity(sendIntent);

				}
			});


		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.rate_menu, menu);
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
			case R.id.menu_rate:
				ShowRateDialog();
				return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
		return true;
	}
	public void showToast(String msg) {
		Toast.makeText(ListingDetails_Activity.this, msg, Toast.LENGTH_LONG).show();
	}
	private AlertDialog SetMessage()
	{

		AlertDialog.Builder alert = new AlertDialog.Builder(
				ListingDetails_Activity.this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			alert = new AlertDialog.Builder(ListingDetails_Activity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			alert = new AlertDialog.Builder(ListingDetails_Activity.this);
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

	private class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		public ImagePagerAdapter() {
			// TODO Auto-generated constructor stub

			inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			return allArrayimages.length;

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			View imageLayout = inflater.inflate(R.layout.imagshow_activity, container, false);
			assert imageLayout != null;

			ImageView gal_imageview=(ImageView)imageLayout.findViewById(R.id.image_place);


			Picasso.with(ListingDetails_Activity.this).load(Constant.SERVER_IMAGE_GALLERY+allArrayimages[position].replace(" ", "%20")).into(gal_imageview);

			container.addView(imageLayout, 0);
			return imageLayout;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}


	public  void  ShowRateDialog()
	{
		final Dialog dialog = new Dialog(ListingDetails_Activity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.ratedialog);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final TextView txtrate=(TextView)dialog.findViewById(R.id.textView1);
		Button btnrate=(Button)dialog.findViewById(R.id.button1);
		RatingBar rating=(RatingBar)dialog.findViewById(R.id.ratingBar1);

		rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
										boolean fromUser) {
				// TODO Auto-generated method stub
				txtrate.setText(String.valueOf(rating));
			}
		});


		btnrate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				if(txtrate.getText().toString().equalsIgnoreCase(""))
				{
					showToast("Please Select AtLeast one Star");
				}
				else
				{
					dialog.dismiss();

					if (JsonUtils.isNetworkAvailable(ListingDetails_Activity.this)) {
						new MyTaskRating().execute(Constant.RATING_URL+itemcatelist.getH_id().toString()+"&device_id="+Constant.DEVICE_ID+"&rate="+txtrate.getText().toString());
 						Log.e("rate",""+Constant.RATING_URL+itemcatelist.getH_id().toString()+"&device_id="+Constant.DEVICE_ID+"&rate="+txtrate.getText().toString());
					} else {
						showToast(getString(R.string.conne_msg1));
						SetMessage();
					}
				}

			}
		});
		dialog.show();
	}
	private	class MyTaskRating extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			return JsonUtils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);



			if (null == result || result.length() == 0) {
				showToast("No data found from web!!!");

			} else {

				try {
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(Constant.CATEGORY_ARRAY_NAME);
					JSONObject objJson = null;
					for (int i = 0; i < jsonArray.length(); i++) {
						objJson = jsonArray.getJSONObject(i);
						rate_msg=objJson.getString(Constant.RATE_MSG);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setAdapterToListviewRate();
			}

		}

		public void setAdapterToListviewRate() {

			showToast(rate_msg);

			if(rate_msg.equals("You have already rated"))
			{

			}
			else
			{

			}

		}
	}

}

