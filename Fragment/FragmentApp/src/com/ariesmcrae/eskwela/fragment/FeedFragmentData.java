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
package com.ariesmcrae.eskwela.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import com.ariesmcrae.eskwela.fragment.R;


/** 
 * Utility class that provides stored Twitter feed data
 * @author aries@ariesmcrae.com 
 * */
public class FeedFragmentData {
	private static final String TAG = "eskwela-FeedFragmentData";
	private static final int[] IDS = { R.raw.ladygaga, R.raw.rebeccablack, R.raw.taylorswift };
	
	private SparseArray<String> mFeeds = new SparseArray<String>();
	private Context mContext;
	
		
	public FeedFragmentData(Context context) {
		mContext = context;
		loadFeeds();
	}

	// Load all stored Twitter feeds into the mFeeds SparseArray. 
	private void loadFeeds() {
		for (int id : IDS) {
			InputStream inputStream = mContext.getResources().openRawResource(id);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			StringBuffer buffer = new StringBuffer("");
			
			try {
				// Read raw data from resource file
				String line = "";
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
			
			} catch (IOException e) {
				Log.i(TAG, "IOException");
			}

			// Convert raw data into a String
			JSONArray feed = null;
			try {
				feed = new JSONArray(buffer.toString());
			} catch (JSONException e) {
				Log.i(TAG, "JSONException");
			}

			mFeeds.put(id, procFeed(feed));
		}
	}
	

	
	// Convert JSON formatted data to a String
	private String procFeed(JSONArray feed) {
		String name = "";
		String tweet = "";

		// string buffer for twitter feeds
		StringBuffer textFeed = new StringBuffer("");

		for (int j = 0; j < feed.length(); j++) {
			try {
				tweet = feed.getJSONObject(j).getString("text");
				JSONObject user = (JSONObject) feed.getJSONObject(j).get("user");
				name = user.getString("name");

			} catch (JSONException e) {
				Log.i(TAG, "JSONException while processing feed");
			}

			textFeed.append(name + " - " + tweet + "\n\n");
		}

		return textFeed.toString();
	}
	

	
	// Return the Twitter feed data for the specified position as a single String
	String getFeed (int position) {
		return mFeeds.get(IDS[position]);
	}
}