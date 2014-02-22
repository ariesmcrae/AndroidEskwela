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
package com.ariesmcrae.androideskwela.celebtweet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.ariesmcrae.androideskwela.celebtweet.R;

/** @author aries@ariesmcrae.com */
public class MainActivity extends Activity implements SelectionListener {

	public static final String TWEET_FILENAME = "tweets.txt";
	public static final String[] FRIENDS = { "taylorswift13", "msrebeccablack", "ladygaga" };
	public static final String DATA_REFRESHED_ACTION = "com.ariesmcrae.androideskwela.celebtweet.DATA_REFRESHED";

	private static final int NUM_FRIENDS = 3;
	private static final String URL_LGAGA = "https://d396qusza40orc.cloudfront.net/android%2FLabs%2FUserNotifications%2Fladygaga.txt";
	private static final String URL_RBLACK = "https://d396qusza40orc.cloudfront.net/android%2FLabs%2FUserNotifications%2Frebeccablack.txt";
	private static final String URL_TSWIFT = "https://d396qusza40orc.cloudfront.net/android%2FLabs%2FUserNotifications%2Ftaylorswift.txt";
	private static final String TAG = "AndroidEskwela-Notifications";
	private static final long TWO_MIN = 2 * 60 * 1000;
	private static final int UNSELECTED = -1;

	private FragmentManager mFragmentManager;
	private FriendsFragment mFriendsFragment;
	private boolean mIsFresh;
	private BroadcastReceiver mRefreshReceiver;
	private int mFeedSelected = UNSELECTED;
	private FeedFragment mFeedFragment;
	private String[] mRawFeeds = new String[3];
	private String[] mProcessedFeeds = new String[3];

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentManager = getFragmentManager();
		addFriendsFragment();

		// The feed is fresh if it was downloaded less than 2 minutes ago
		mIsFresh = (System.currentTimeMillis() - getFileStreamPath(TWEET_FILENAME).lastModified()) < TWO_MIN;

		ensureData();
	}

	
	
	/** Add Friends Fragment to Activity */
	private void addFriendsFragment() {
		mFriendsFragment = new FriendsFragment();
		mFriendsFragment.setArguments(getIntent().getExtras());

		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.add(R.id.fragment_container, mFriendsFragment);

		transaction.commit();
	}
	
	

	/**
	 * If stored Tweets are not fresh, reload them from network, Otherwise, load
	 * them from file
	 */
	private void ensureData() {
		log("In ensureData(), mIsFresh:" + mIsFresh);

		if (!mIsFresh) {
			Toast.makeText(getApplicationContext(), "Downloading Tweets from Network", Toast.LENGTH_LONG).show();

			log("Issuing Toast Message");

			// Start new AsyncTask to download Tweets from network
			new DownloaderTask(this).execute(URL_LGAGA, URL_RBLACK, URL_TSWIFT);

			// Set up a BroadcastReceiver to receive an Intent when download
			// finishes.
			mRefreshReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					log("BroadcastIntent received in MainActivity");

					if (isOrderedBroadcast()) {
						// Check to make sure this is an ordered broadcast. 
						// Let AsyncTask sender (DownloaderTask) know that the Intent was received by setting result code to RESULT_OK.
						// RESULT_OK which will inform the AsyncTask that the MainActivity is active and in the foreground, and
						// therefore, the AsyncTask should not send the user notification.
						setResultCode(Activity.RESULT_OK);
					}
				}
			};

		} else {
			loadTweetsFromFile();
			parseJSON();
			updateFeed();
		}
	}

	
	
	/** Called when new Tweets have been downloaded */
	public void setRefreshed(String[] feeds) {
		mRawFeeds[0] = feeds[0];
		mRawFeeds[1] = feeds[1];
		mRawFeeds[2] = feeds[2];

		parseJSON();
		updateFeed();
		mIsFresh = true;
	};


	/** Called when a Friend is clicked on */
	@Override
	public void onItemSelected(int position) {
		mFeedSelected = position;
		mFeedFragment = addFeedFragment();

		if (mIsFresh) {
			updateFeed();
		}
	}

	
	
	/**
	 * Calls FeedFragement.update, passing in the tweets for the currently selected friend
	 */
	void updateFeed() {
		if (null != mFeedFragment)
			mFeedFragment.update(mProcessedFeeds[mFeedSelected]);
	}

	
	
	/** Add FeedFragment to Activity */
	private FeedFragment addFeedFragment() {
		FeedFragment feedFragment;
		feedFragment = new FeedFragment();

		FragmentTransaction transaction = mFragmentManager.beginTransaction();

		transaction.replace(R.id.fragment_container, feedFragment);
		transaction.addToBackStack(null);

		transaction.commit();
		mFragmentManager.executePendingTransactions();
		return feedFragment;
	}

	
	
	/** Register the BroadcastReceiver */
	@Override
	protected void onResume() {
		super.onResume();

		if (mRefreshReceiver != null) {
			// mRefreshReciver is null if mIsFresh == true, therefore we need to check for null.
			registerReceiver(mRefreshReceiver, new IntentFilter(DATA_REFRESHED_ACTION));
		}
	}

	
	
	@Override
	protected void onPause() {
		if (mRefreshReceiver != null) {
			// mRefreshReciver is null if mIsFresh == true, therefore we need to
			// check for null.
			unregisterReceiver(mRefreshReceiver);
		}

		super.onPause();
	}

	
	
	/** Convert raw Tweet data (in JSON format) into text for display */
	public void parseJSON() {
		JSONArray[] JSONFeeds = new JSONArray[NUM_FRIENDS];

		for (int i = 0; i < NUM_FRIENDS; i++) {
			try {
				JSONFeeds[i] = new JSONArray(mRawFeeds[i]);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			String name = "";
			String tweet = "";

			JSONArray tmp = JSONFeeds[i];

			// string buffer for twitter feeds
			StringBuffer tweetRec = new StringBuffer("");

			for (int j = 0; j < tmp.length(); j++) {
				try {
					tweet = tmp.getJSONObject(j).getString("text");
					JSONObject user = (JSONObject) tmp.getJSONObject(j).get("user");
					name = user.getString("name");

				} catch (JSONException e) {
					e.printStackTrace();
				}

				tweetRec.append(name + " - " + tweet + "\n\n");
			}

			mProcessedFeeds[i] = tweetRec.toString();

		}
	}

	
	
	// Retrieve feeds text from a file Store them in mRawTextFeed[]
	private void loadTweetsFromFile() {
		BufferedReader reader = null;

		try {
			FileInputStream fis = openFileInput(TWEET_FILENAME);
			reader = new BufferedReader(new InputStreamReader(fis));
			String s = null;
			int i = 0;
			while (null != (s = reader.readLine()) && i < NUM_FRIENDS) {
				mRawFeeds[i] = s;
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
