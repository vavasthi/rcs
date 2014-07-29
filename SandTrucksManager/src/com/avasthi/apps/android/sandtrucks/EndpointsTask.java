package com.avasthi.apps.android.sandtrucks;

import java.io.IOException;

import com.avasthi.apps.android.sandtrucks.businessendpoint.Businessendpoint;
import com.avasthi.apps.android.sandtrucks.businessendpoint.model.Business;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.content.Context;
import android.os.AsyncTask;

public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
	protected Long doInBackground(Context... contexts) {

		Businessendpoint.Builder endpointBuilder = new Businessendpoint.Builder(
				AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					@Override
					public void initialize(HttpRequest httpRequest)
							throws IOException {
						// TODO Auto-generated method stub

					}
				});
		Businessendpoint endpoint = CloudEndpointUtils.updateBuilder(
				endpointBuilder).build();
		try {
			Business b = new Business().setEmail("vinay@avasthi.com");
			b.setId(0L);
			b.setName("My Business");
			b.setPoc("Vinay Avasthi");

			Business result = endpoint.insertBusiness(b).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (long) 0;
	}
}