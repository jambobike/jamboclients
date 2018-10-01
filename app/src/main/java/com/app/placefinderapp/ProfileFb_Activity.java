package com.app.placefinderapp;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;


public class ProfileFb_Activity extends ActionBarActivity {

	EditText txt_email,txt_name;
	MyApplication App;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilefb_activity);
		Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		toolbar.setTitle("Profile");
		this.setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		App = MyApplication.getInstance();

		txt_email=(EditText)findViewById(R.id.edt_email);
		txt_name=(EditText)findViewById(R.id.edt_user);

		txt_email.setText(App.getUserEmail().replace("%20", " "));
		txt_name.setText(App.getUserName().replace("%20", " "));
		txt_email.setEnabled(false);
		txt_name.setEnabled(false);

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
