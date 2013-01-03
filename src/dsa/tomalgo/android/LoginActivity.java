package dsa.tomalgo.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import dsa.tomalgo.android.R;
import dsa.tomalgo.android.api.TomalgoServiceApi;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private final static String TAG = "LoginActivity";
	private final static int ID_DIALOG_FETCHING = 0;
	private final static int ID_DIALOG_EXCEPTION = 1;
	public String username;
	public String password;

	private class LoginTask extends AsyncTask<String, Void, JSONObject> {
		private final static String TAG = "LoginTask";
		private Activity activity = null;

		LoginTask(Activity activity) {
			super();
			this.activity = activity;
			Log.d(TAG, "LoginTask Constructor");
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonobject = null;
			try {
				Log.d(TAG, "LoginTask doInBackground");
				String content[] = TomalgoServiceApi.getInstance(
						getApplicationContext()).login(params[0], params[1]);
				for (int i = 0; i < content.length; i++)
					Log.d(LoginTask.TAG, content[i]);

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

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(JSONObject jsonobject) {
			dismissDialog(ID_DIALOG_FETCHING);
			try {
				String status = (String) jsonobject.get("status");
				if (status.equals("KO")) {
					Log.d(TAG, "Login KO");
					Log.d(TAG, (String) jsonobject.get("result"));
					AlertDialog.Builder builder = new AlertDialog.Builder(
							activity);
					builder.setMessage((String) jsonobject.get("result"))
					.setTitle("Format Error");
					builder.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int id) {
							dialog.dismiss();

						}
					});
					builder.create().show();
				} else {
					Log.d(TAG, "Login OK");
					JSONObject result = (JSONObject) jsonobject.get("result");
					String succeed = (String) result.get("succeed").toString();
					Log.d(TAG, "succeed: " +succeed);

					if (succeed.equals("true")){
						String enterprise = (String) result.get("enterprise").toString();
						Log.d(TAG, ("enterprise: " +enterprise));
						Intent intent = new Intent(activity, MainLayoutActivity.class);
						Log.d(TAG, intent.toString());
						intent.putExtra("username", username);
						intent.putExtra("password", password);
						Log.d(TAG, "Antes");
						startActivity(intent);
						Log.d(TAG, "Despues");

					}else {
						String message = (String) result.get("message").toString();
						Log.d(TAG, ("message: " +message));
						AlertDialog.Builder builder = new AlertDialog.Builder(
								activity);
						builder.setMessage((String) result.get("message"))
						.setTitle("Authentication Error");
						builder.setPositiveButton("Aceptar",
								new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int id) {
								dialog.dismiss();
							}
						});
						builder.create().show();						
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
	}

	public void showRegisterActivity(View v) {
		Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
		startActivity(i);
	}

	public void loginClicked(View v) {
		username = ((EditText) findViewById(R.id.etUsername)).getText()
				.toString();
		password = ((EditText) findViewById(R.id.etPassword)).getText()
				.toString();
		Log.d(TAG, username + "/" + password);
		showDialog(ID_DIALOG_FETCHING);
		(new LoginTask(this)).execute(username, password);

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
			loadingDialog.setMessage("Trying to login...");
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(false);
			return loadingDialog;

		case ID_DIALOG_EXCEPTION:

			break;
		}
		return super.onCreateDialog(id);
	}
}
