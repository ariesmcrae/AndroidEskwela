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

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import com.ariesmcrae.eskwela.fragment.R;

/** @author aries@ariesmcrae.com */
public class MainActivity extends Activity implements FriendsFragment.SelectionListener {

	private static final String TAG = "eskwela-Fragment";

	private FriendsFragment mFriendsFragment;
	private FeedFragment mFeedFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// If the layout is single-pane, create the FriendsFragment and add it to the Activity
		if (!isInTwoPaneMode()) {
			//single-pane
			mFriendsFragment = new FriendsFragment();

			FragmentTransaction fragmentTransation = getFragmentManager().beginTransaction();
			fragmentTransation.add(R.id.fragment_container, mFriendsFragment);
			fragmentTransation.commit();
			
		} else {
			// double-pane : save a reference to the FeedFragment for later use
			mFeedFragment = (FeedFragment) getFragmentManager().findFragmentById(R.id.feed_frag);
		}
	}

	
	// If there is no fragment_container ID, then the application is in two-pane mode
	private boolean isInTwoPaneMode() {
		return findViewById(R.id.fragment_container) == null;
	}


	
	// Display selected Twitter feed
	public void onItemSelected(int position) {
		Log.i(TAG, "Entered onItemSelected(" + position + ")");

		// If there is no FeedFragment instance, then create one
		if (mFeedFragment == null)
			mFeedFragment = new FeedFragment();

		// If in single-pane mode, replace single visible Fragment
		if (!isInTwoPaneMode()) {
			//single-pane
			FragmentTransaction fragmentTransation = getFragmentManager().beginTransaction();
			fragmentTransation.replace(R.id.fragment_container, mFeedFragment);
			fragmentTransation.addToBackStack(null);
			fragmentTransation.commit();  			
			
			// execute transaction now
			getFragmentManager().executePendingTransactions();
		}

		// Update Twitter feed display on FriendFragment
		mFeedFragment.updateFeedDisplay(position);
	}

}
