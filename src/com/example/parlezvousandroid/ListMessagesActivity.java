package com.example.parlezvousandroid;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.parlezvousandroid.MainActivity.ParlezVousTask;
import com.example.parlezvousandroid.utils.InputStreamToString;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ListMessagesActivity extends Activity {

	private final String TAG = ListMessagesActivity.class.getSimpleName();
	
	private ListView listMessages;
	private Intent intent;
	
	private MessageTask task;
	
	private ProgressBar progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.activity_list_messages);

		progressDialog = (ProgressBar) findViewById(R.id.loading);
		
		listMessages = (ListView) findViewById(R.id.list_messages);
		Log.d(TAG, "listMessages : "+listMessages.toString());
		
		intent = getIntent();
				
		task = new MessageTask();
		task.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_messages, menu);
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
	
	class MessageTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			progressDialog.setVisibility(View.VISIBLE);
			progressDialog.animate();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			
			Log.d(TAG, "doInBackground");

			String usernameStr = intent.getStringExtra("username");
			String passwordStr = intent.getStringExtra("password");

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
			
			Log.d(TAG, "onPostExecute");
			
			progressDialog.setVisibility(View.INVISIBLE);
			
			String[] author  = {"aut1","aut2","aut3"};
			String[] message = {"msg1","msg2","msg3"};
			
			listMessages.setAdapter(new DataListAdapter(author,message));
			
		}
		
	}

	class DataListAdapter extends BaseAdapter {

		private String[] author;
		private String[] message;

		public DataListAdapter() {
			author = null;
			message = null;
		}

		public DataListAdapter(String[] a, String[] m) {
			Log.d(TAG, "on dataListAdapter Constructor");
			author = a;
			message = m;
		}

		@Override
		public int getCount() {
			return author.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = getLayoutInflater();
			View row;
			row = inflater.inflate(R.layout.activity_list_messages, parent,false);
			TextView authorView , messageView;
			
			
			return null;
		}

	}

}
