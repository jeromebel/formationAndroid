package com.example.parlezvousandroid;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parlezvousandroid.utils.InputStreamToString;

public class MainActivity extends Activity {

	private final String TAG = MainActivity.class.getSimpleName();

	private EditText usernameField;
	private EditText passwordField;

	private Button buttonVider;
	private Button buttonEnvoyer;

	private ProgressBar progressDialog;

	private ParlezVousTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.i(TAG, "onCreate");

		setContentView(R.layout.activity_main);
		usernameField = (EditText) findViewById(R.id.edit_username);
		passwordField = (EditText) findViewById(R.id.edit_password);

		buttonEnvoyer = (Button) findViewById(R.id.button_submit);
		buttonEnvoyer.setOnClickListener(myListener);

		buttonVider = (Button) findViewById(R.id.button_clean);
		buttonVider.setOnClickListener(myListener);

		progressDialog = (ProgressBar) findViewById(R.id.loading);

	}

	OnClickListener myListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i(TAG, "onClickLitener");
			if (v.getId() == R.id.button_submit) {
				if (task == null) {
					task = new ParlezVousTask();
					task.execute();
				}
				else
					Log.d(TAG, "secured recalled task");

			} else if (v.getId() == R.id.button_clean) {
				Toast.makeText(getBaseContext(), "Vider", Toast.LENGTH_SHORT)
						.show();
				usernameField.setText("");
				passwordField.setText("");
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		Log.i(TAG, "onCreateOptions");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.i(TAG, "onSaveInstanceState");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "OnPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	public class ParlezVousTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			progressDialog.setVisibility(View.VISIBLE);
			progressDialog.animate();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			String usernameStr = usernameField.getText().toString();
			String passwordStr = passwordField.getText().toString();

			String url = "http://dev.loicortola.com/parlez-vous-android/connect/"
					+ usernameStr + "/" + passwordStr;

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response;
			try {
				response = client.execute(request);
				InputStream content = response.getEntity().getContent();

				String compStr = InputStreamToString.convert(content);

				if ("true".equals(compStr))
					return true;

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.setVisibility(View.INVISIBLE);

			if (result) {
				Toast.makeText(getBaseContext(), "c'est bon ",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(),
						Logged.class);
				intent.putExtra("username", usernameField.getText().toString());
				intent.putExtra("password", passwordField.getText().toString());
				startActivity(intent);

			} else
				Toast.makeText(getBaseContext(), "c'est mauvais ",
						Toast.LENGTH_SHORT).show();
		}
		
	}

}
