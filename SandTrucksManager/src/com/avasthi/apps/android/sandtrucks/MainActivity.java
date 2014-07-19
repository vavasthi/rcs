package com.avasthi.apps.android.sandtrucks;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;

/**
 * The Main Activity.
 * 
 * This activity starts up the RegisterActivity immediately, which communicates
 * with your App Engine backend using Cloud Endpoints. It also receives push
 * notifications from backend via Google Cloud Messaging (GCM).
 * 
 * Check out RegisterActivity.java for more details.
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

//		new EndpointsTask().execute(getApplicationContext());
		
		// Start up RegisterActivity right away
//		Intent intent = new Intent(this, RegisterActivity.class);
//		startActivity(intent);
		// Since this is just a wrapper to start the main activity,
		// finish it after launching RegisterActivity
//		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}	
}
