package com.app.placefinderapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

public class Splash_Activity extends ActionBarActivity {
 
	protected boolean active=true;
	protected int splashTime=2000;
	MyApplication App;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_fragment);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(getResources().getColor(R.color.m_color_primary_dark));
		}
		App = MyApplication.getInstance();
		 Thread splashThread=new Thread()
	        {
	        	public void run()
	        	{
	        		try
	        		{
	        			int waited=0;
	        			
	        			while(active && (waited<splashTime))
	        			{
	        					sleep(100);
	        					if(active)
	        					{
	        						waited +=100;
	        					}
	        			}
	        		}
	        		catch(Exception e)
	        		{
	        			e.toString();
	        		}
	        	 	
	        	finally	
	        	{
					if(App.getIsLogin())
					{
						Intent int1=new Intent(getApplicationContext(),Category_Activity.class);
						int1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(int1);
						finish();
					}
					else
					{
						Intent intplay = new Intent(getApplicationContext(), SignInActivity.class);
						startActivity(intplay);
						finish();
					}

 	        	}
	        	}
	        };
	    
	      splashThread.start();
 	}
 }
