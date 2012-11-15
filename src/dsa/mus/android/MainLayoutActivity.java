package dsa.mus.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dsa.mus.android.api.MusServiceApi;
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

	private class FetchGamesList extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPostExecute(JSONObject jsonobject) {

			try {
				String status = (String) jsonobject.get("status");
				if (status.equals("OK")) {
					array = jsonobject.getJSONArray("result");
					String[] values = new String[array.length()];
					for (int i = 0; i < array.length(); i++) {
						JSONObject game = array.getJSONObject(i);
						values[i] = game.getString("player0") + " & "
								+ game.getString("player2") + " vs. "
								+ game.getString("player1") + " & "
								+ game.getString("player3");
						Log.d(TAG, game.getInt("gameid") + "");
						Log.d(TAG, game.getString("player0"));
						Log.d(TAG, game.getString("player1"));
						Log.d(TAG, game.getString("player2"));
						Log.d(TAG, game.getString("player3"));
						Log.d(TAG, game.getString("score"));
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

		private final static String TAG = "FetchGamesList";

		FetchGamesList() {
			super();

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonobject = null;
			try {
				Log.d(TAG, "FetchGamesList doInBackground");
				String content[] = MusServiceApi.getInstance(
						getApplicationContext()).listGames(params[0]);
				for (int i = 0; i < content.length; i++)
					Log.d(FetchGamesList.TAG, content[i]);

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
		Log.d(TAG, bundle.get("id").toString());

		showDialog(ID_DIALOG_FETCHING);
		(new FetchGamesList()).execute(bundle.getString("username"));

	}

	private void showList() {
		ListView listView = (ListView) findViewById(R.id.gameslist);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View parent,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(),
						ScoreActivity.class);
				try {
					Log.d(TAG, array.getJSONObject(position).toString());
					JSONObject game = array.getJSONObject(position);
					intent.putExtra("players0", game.getString("player0")
							+ " & " + game.getString("player2"));
					intent.putExtra("players1", game.getString("player1")
							+ " & " + game.getString("player3"));
					intent.putExtra("score", game.getString("score"));
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
			loadingDialog.setMessage("Fetching games...");
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