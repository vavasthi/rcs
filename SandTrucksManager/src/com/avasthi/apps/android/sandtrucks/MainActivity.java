package com.avasthi.apps.android.sandtrucks;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


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

    static final int PICK_CONTACT_REQUEST = 1;
    static final String PREF_ACCOUNT_NAME = "STM_ACCOUNT_NAME";
    static final String PREF_REGISTERED = "STM_REGISTERED";
    private String accountName_ = null;
	private boolean registered_ = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		checkForAuthentication();
		Toast.makeText(this, "Email is " + accountName_ + " Registsred is " + registered_, Toast.LENGTH_LONG).show();
		new MembershipTask(accountName_, this).execute(new String[0]);
		if (accountName_ != null && !registered_) {
			
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.initial_registration_dialog, null, false);
			TextView tv = (TextView)v.findViewById(R.id.email);
			tv.setText(accountName_);
			setContentView(v);
		}
		else {
			
			setContentView(R.layout.activity_main);
		}
//		new EndpointsTask().execute(getApplicationContext());
		
		// Start up RegisterActivity right away
//		Intent intent = new Intent(this, RegisterActivity.class);
//		startActivity(intent);
		// Since this is just a wrapper to start the main activity,
		// finish it after launching RegisterActivity
//		finish();
	}
    private boolean playServicesAvailable() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            return true;
        }
        else {
            return false;
        }
    }
    private void checkForAuthentication() {

        SharedPreferences settings;
        if (!playServicesAvailable()) {
            Toast.makeText(this, "Play services not available.", Toast.LENGTH_LONG).show();
        }
        else {

            settings = getSharedPreferences("SandTrucksManagerMainActivity", Context.MODE_PRIVATE);
            boolean accountExists = false;
            if (settings != null) {

                if (settings.contains(PREF_ACCOUNT_NAME)) {
                    accountName_ = settings.getString(PREF_ACCOUNT_NAME,null);
                    if (accountName_ != null) {
                        accountExists = true;
                    }
                    registered_ = settings.getBoolean(PREF_REGISTERED, Boolean.FALSE);
                }
            }
            if (!accountExists) {

                Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
                        true, null, null, null, null);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
            accountName_ = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.d("AccountPicker", "Account Name=" + accountName_);
            SharedPreferences.Editor e = getSharedPreferences("SandTrucksManagerMainActivity", Context.MODE_PRIVATE).edit();
            e.putString(PREF_ACCOUNT_NAME, accountName_);
            e.commit();
        }
    }


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}	
}
