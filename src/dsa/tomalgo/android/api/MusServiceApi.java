package dsa.tomalgo.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

public class MusServiceApi {
	private final static String TAG = "MusServiceApi";
	private HttpClient httpclient = null;
	private String uri = null;
	private static MusServiceApi instance = null;

	private MusServiceApi(Context ctx) throws IOException {
		super();

		httpclient = new DefaultHttpClient();
		InputStream is = ctx.getResources().getAssets().open("api.properties");
		Properties p = new Properties();
		p.load(is);
		uri = p.getProperty("uri");
	}

	public static MusServiceApi getInstance(Context ctx) throws IOException {
		if (instance == null)
			instance = new MusServiceApi(ctx);
		return instance;
	}

	public String[] login(String username, String password)
			throws ClientProtocolException, IOException {
		
		String sha1password = SHA1.getInstance().digestToString(password);
		
		HttpGet request = new HttpGet();
		try {
			URI reqURI = new URI(uri + "action=login&username=" + username
					+ "&password=" + sha1password);
			request.setURI(reqURI);
			HttpResponse response = httpclient.execute(request);
			Log.d(TAG, response.getStatusLine().getReasonPhrase() + " - "
					+ response.getStatusLine().getStatusCode());

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			Vector<String> lines = new Vector<String>();
			String line = null;
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[lines.size()]);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Please verify uri value in assets/api.properties");
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] listGames(String password) throws ClientProtocolException, IOException{
		Log.d(TAG, "listGames");
		String sha1password = SHA1.getInstance().digestToString(password);
		HttpGet request = new HttpGet();
		try {
			URI reqURI = new URI(uri + "action=queryevents&password=" + sha1password);
			request.setURI(reqURI);
			HttpResponse response = httpclient.execute(request);
			Log.d(TAG, response.getStatusLine().getReasonPhrase() + " - "
					+ response.getStatusLine().getStatusCode());

			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			Vector<String> lines = new Vector<String>();
			String line = null;
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines.toArray(new String[lines.size()]);
		} catch (URISyntaxException e) {
			Log.e(TAG, "Please verify uri value in assets/api.properties");
			e.printStackTrace();
		}
		return null;
	}
}
