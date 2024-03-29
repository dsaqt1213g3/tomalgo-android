package dsa.tomalgo.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dsa.tomalgo.android.R;
import dsa.tomalgo.android.api.TomalgoServiceApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainLayoutActivity extends Activity {
	private final static String TAG = "MainLayoutActivity";
	private final static int ID_DIALOG_FETCHING = 0;

	private String[] values;
	private JSONArray array;
	public String username;
	public String password;

	private class FetchEventsList extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPostExecute(JSONObject jsonobject) {

			try {
				String status = (String) jsonobject.get("status");
				if (status.equals("OK")) {
					Log.d(TAG,"Recibido OK");
					array = jsonobject.getJSONArray("result");
					String[] values = new String[array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject event = array.getJSONObject(i);
						values[i] = event.getString("text");
						Log.d(TAG, event.getString("enterprise"));
					}
					setValues(values);
					dismissDialog(ID_DIALOG_FETCHING);
					showList();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private final static String TAG = "FetchEventsList";

		FetchEventsList() {
			super();

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonobject = null;
			try {
				Log.d(TAG, "FetchEventsList doInBackground, params[0]: " +params[0]);
				String content[] = TomalgoServiceApi.getInstance(
						getApplicationContext()).listEvents(params[0]);
				for (int i = 0; i < content.length; i++)
					Log.d(FetchEventsList.TAG, content[i]);

				String json = content[content.length - 1];
				jsonobject = new JSONObject(json);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonobject;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		Bundle bundle = this.getIntent().getExtras();
		username = bundle.get("username").toString();
		password = bundle.get("password").toString();
		Log.d(TAG, username+" / "+password);

		showDialog(ID_DIALOG_FETCHING);
		(new FetchEventsList()).execute(bundle.getString("password"));

	}

	private void showList() {
		ListView listView = (ListView) findViewById(R.id.eventslist);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> view, View parent,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						EventsActivity.class);
				try {
					Log.d(TAG, array.getJSONObject(position).toString());
					JSONObject events = array.getJSONObject(position);
					intent.putExtra("promo", events.getString("promo"));
					intent.putExtra("eventID", events.getString("id"));
					intent.putExtra("username", username);
					intent.putExtra("password", password);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_layout, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case ID_DIALOG_FETCHING:
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage("Fetching events...");
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(false);
			return loadingDialog;

		}
		return super.onCreateDialog(id);
	}

	private void setValues(String[] values) {
		this.values = values;
	}
}
