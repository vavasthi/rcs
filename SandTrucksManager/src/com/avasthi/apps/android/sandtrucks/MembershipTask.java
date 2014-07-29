package com.avasthi.apps.android.sandtrucks;

import java.io.IOException;
import java.util.List;

import sandtrucks.android.apps.avasthi.com.sandTrucksSystemApi.SandTrucksSystemApi;
import sandtrucks.android.apps.avasthi.com.sandTrucksSystemApi.SandTrucksSystemApi.DoesUserExistAndAssociated;
import sandtrucks.android.apps.avasthi.com.sandTrucksSystemApi.model.SandTrucksManagerBackendApiResponse;

import com.avasthi.apps.android.sandtrucks.businessendpoint.Businessendpoint;
import com.avasthi.apps.android.sandtrucks.businessendpoint.Businessendpoint.ListBusiness;
import com.avasthi.apps.android.sandtrucks.businessendpoint.model.Business;
import com.avasthi.apps.android.sandtrucks.businessendpoint.model.CollectionResponseBusiness;
import com.avasthi.apps.android.sandtrucks.businessmembershipendpoint.Businessmembershipendpoint;
import com.avasthi.apps.android.sandtrucks.businessmembershipendpoint.model.BusinessMembership;
import com.avasthi.apps.android.sandtrucks.userendpoint.Userendpoint;
import com.avasthi.apps.android.sandtrucks.userendpoint.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class MembershipTask extends AsyncTask<String, Void, Void>{

	private final String email_;
	ProgressDialog dialog_;
	
	static final int SUCCESS = 0x00;
	static final int USER_NOT_FOUND = 0x01;
	static final int USER_NOT_ASSOCIATED = 0x02;
	
	public MembershipTask(String email, Activity activity) {
		email_ = email;
		dialog_ = new ProgressDialog(activity);
		dialog_.setTitle(activity.getText(R.string.please_wait));
		dialog_.setTitle(activity.getText(R.string.please_wait_message));
		dialog_.setProgressStyle(ProgressDialog.STYLE_SPINNER);;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog_.show();
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog_.hide();
	}
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		Userendpoint.Builder ub 
		= new Userendpoint.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
		try {
			SandTrucksSystemApi.Builder sb 
			= new SandTrucksSystemApi.Builder(AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
			SandTrucksManagerBackendApiResponse sbr = sb.build().doesUserExistAndAssociated(email_).execute();
			if (sbr.getStatus() != true) {
				if (sbr.getCode() == USER_NOT_FOUND) {
					// Perform User Creation
					Log.i("User", "User not found");
				}
				else {
					// Perform User Association
					Log.i("User", "User not Association");
				}
			}
		}
		catch(IOException ioex) {
			ioex.printStackTrace();
		}
		return null;
	}
	

}
