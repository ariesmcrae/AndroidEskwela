/**
 The MIT License (MIT)

 Copyright (c) 2014 Aries McRae

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 **/
package com.ariesmcrae.eskwela.celebtweet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import com.ariesmcrae.eskwela.celebtweet.R;

/** @author aries@ariesmcrae.com */
public class DownloaderTask extends AsyncTask<String, Void, String[]> {

	private static final int SIM_NETWORK_DELAY = 5000;
	private static final String TAG = "AndroidEskwela-Notifications";
	private final int MY_NOTIFICATION_ID = 11151990;
	private String mFeeds[] = new String[3];
	private MainActivity mParentActivity;
	private Context mApplicationContext;

	// Change this variable to false if you do not have a stable network connection
	private static final boolean HAS_NETWORK_CONNECTION = false;

	// Raw feed file IDs used if you do not have a stable connection
	public static final int txtFeeds[] = { R.raw.tswift, R.raw.rblack, R.raw.lgaga };

	
	
	public DownloaderTask(MainActivity parentActivity) {
		super();

		mParentActivity = parentActivity;
		mApplicationContext = parentActivity.getApplicationContext();
	}

	
	
	@Override
	protected String[] doInBackground(String... urlParameters) {
		log("Entered doInBackground()");

		return download(urlParameters);
	}

	
	
	private String[] download(String urlParameters[]) {
		boolean downloadCompleted = false;

		try {
			for (int idx = 0; idx < urlParameters.length; idx++) {

				URL url = new URL(urlParameters[idx]);
				try {
					Thread.sleep(SIM_NETWORK_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				InputStream inputStream;
				BufferedReader in;

				// Alternative for students without a network connection
				if (HAS_NETWORK_CONNECTION) {
					inputStream = url.openStream();
					in = new BufferedReader(new InputStreamReader(inputStream));
				} else {
					inputStream = mApplicationContext.getResources().openRawResource(txtFeeds[idx]);
					in = new BufferedReader(new InputStreamReader(inputStream));
				}

				String readLine;
				StringBuffer buf = new StringBuffer();

				while ((readLine = in.readLine()) != null) {
					buf.append(readLine);
				}

				mFeeds[idx] = buf.toString();

				if (null != in) {
					in.close();
				}
			}

			downloadCompleted = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		log("Tweet Download Completed:" + downloadCompleted);

		notify(downloadCompleted);

		return mFeeds;
	}

	
	
	// Call back to the MainActivity to update the feed display
	@Override
	protected void onPostExecute(String[] result) {
		super.onPostExecute(result);

		if (mParentActivity != null) {
			mParentActivity.setRefreshed(result);
		}
	}
	
	

	// If necessary, notifies the user that the tweet downloads are complete.
	// Sends an ordered broadcast back to the BroadcastReceiver in MainActivity
	// to determine whether the notification is necessary.
	private void notify(final boolean success) {
		log("Entered notify()");

		final Intent restartMainActivtyIntent = new Intent(mApplicationContext, MainActivity.class);
		restartMainActivtyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		if (success) {
			// Save tweets to a file
			saveTweetsToFile();
		}

		// Sends an ordered broadcast to determine whether MainActivity is active and in the foreground. 
		// Creates a new BroadcastReceiver to receive a result indicating the state of MainActivity

		// The Action for this broadcast Intent is MainActivity.DATA_REFRESHED_ACTION.
		// The result Activity.RESULT_OK, indicates that MainActivity is active and in the foreground.
		mApplicationContext.sendOrderedBroadcast(new Intent(MainActivity.DATA_REFRESHED_ACTION), null, new BroadcastReceiver() {
			final String failMsg = "Download has failed. Please retry Later.";
			final String successMsg = "Download completed successfully.";

			@Override
			public void onReceive(Context context, Intent intent) {
				log("Entered result receiver's onReceive() method");

				// Check whether the result code is RESULT_OK. 
				// If not OK, BroadcastReciever should create a Notification Area Notification.
				if (getResultCode() != Activity.RESULT_OK) {
					final PendingIntent pendingIntent = PendingIntent.getActivity(mApplicationContext, 0, restartMainActivtyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

					RemoteViews mContentView = new RemoteViews(mApplicationContext.getPackageName(), R.layout.custom_notification);
					mContentView.setTextViewText(R.id.text, success == true ? successMsg : failMsg);

					Notification.Builder notificationBuilder = new Notification.Builder(mApplicationContext)
							.setContentTitle("CelebTweet")
							.setSmallIcon(android.R.drawable.stat_sys_warning)
							.setAutoCancel(true)
							.setContentIntent(pendingIntent)
							.setContentText("CelebTweet").setContent(mContentView);

					NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					// mId allows you to update the notification later on.					
					notificationManager.notify(MY_NOTIFICATION_ID, notificationBuilder.build()); 
					log("Notification Area Notification sent");
				}
			}
		}, null, 0, null, null);
	}

	
	
	
	// Saves the tweets to a file
	private void saveTweetsToFile() {
		PrintWriter writer = null;
		try {
			FileOutputStream fos = mApplicationContext.openFileOutput(MainActivity.TWEET_FILENAME, Context.MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));

			for (String s : mFeeds) {
				writer.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}

	
	
	
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}