package dsa.tomalgo.android;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import dsa.tomalgo.android.R;
import dsa.tomalgo.android.api.TomalgoServiceApi;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class EventsActivity extends Activity {
	private final static String TAG = "EventsActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		setContentView(R.layout.events_layout);

		String promo = bundle.getString("promo");
		String eventID = bundle.getString("eventID");
		String username = bundle.getString("username");
		String password = bundle.getString("password");
		Log.d(TAG, "Promo: "+promo+"; eventID: "+eventID+"; username: "+username+"; password: "+password);

		if (promo.equals("false")){
			((TextView) findViewById(R.id.PromoResult)).setText("Este evento no admite confirmacion de asistencia " +username);
		}else {
			JSONObject event = null;
			try {
				Log.d(TAG, "ConfirmEvent");
				String content[] = TomalgoServiceApi.getInstance(
						getApplicationContext()).ConfirmEvent(password, eventID);
				for (int i = 0; i < content.length; i++)
					Log.d(TAG, content[i]);

				String json = content[content.length - 1];
				event = new JSONObject(json);
				String status = event.getString("status");
				//String result = event.getString("result");

				if (status.equals("OK") && event.getBoolean("result")){
					((TextView) findViewById(R.id.PromoResult)).setText(
							"Has confirmado asistencia al evento correctamente. TE ESPERAMOS "+username+"!!!");
				}else {
					((TextView) findViewById(R.id.PromoResult)).setText(
							"Ya habías confirmado asistencia al evento. TE ESPERAMOS "+username+"!!!");
				}

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

		}


		//		((TextView) findViewById(R.id.tvPlayers1)).setText(bundle
		//				.getString("players1"));
		//		((TextView) findViewById(R.id.tvScore)).setText(bundle
		//				.getString("score"));
	}
}
