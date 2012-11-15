package dsa.mus.android;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class ScoreActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();

		setContentView(R.layout.score_layout);
		((TextView) findViewById(R.id.tvPlayers0)).setText(bundle
				.getString("players0"));
		((TextView) findViewById(R.id.tvPlayers1)).setText(bundle
				.getString("players1"));
		((TextView) findViewById(R.id.tvScore)).setText(bundle
				.getString("score"));
	}
}
