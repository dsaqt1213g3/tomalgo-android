package dsa.tomalgo.android;

import dsa.tomalgo.android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class RegisterActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
	}

	public void returnToLoginActivity(View v) {
		finish();
	}
}
