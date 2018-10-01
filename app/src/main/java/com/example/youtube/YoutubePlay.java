package com.example.youtube;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.placefinderapp.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
 
public class YoutubePlay extends YouTubeFailureRecoveryActivity {

	private String id;
	Toolbar toolbar;
	ImageView imageView;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youtube);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
  		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		} 
 		imageView=(ImageView)findViewById(R.id.imageView1);

		Bundle b = getIntent().getExtras();
		id = (String) b.get("id");
		Log.e("id", id);
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView
				.initialize(
						getString(R.string.youtube_api_key),
						this);
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		// TODO Auto-generated method stub
		if (!wasRestored) {
			player.loadVideo(id);
		}
		 
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		// TODO Auto-generated method stub
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}
}