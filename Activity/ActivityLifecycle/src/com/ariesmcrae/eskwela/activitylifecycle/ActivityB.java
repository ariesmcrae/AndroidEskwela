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
package com.ariesmcrae.eskwela.activitylifecycle;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/** @author aries@ariesmcrae.com */
public class ActivityB extends Activity {

	private static final String CREATE_KEY = "create";
	private static final String START_KEY = "start";
	private static final String RESUME_KEY = "resume";
	private static final String RESTART_KEY = "restart";

	// String for LogCat documentation
	private final static String TAG = "eskwela-ActivityLifecycleB";

	// Lifecycle counters
	// Counter variables for onCreate(), onRestart(), onStart() and onResume(), called mCreate, etc.
	// Increment these variables' values when their corresponding lifecycle methods get called
	private int mCreate = 0;
	private int mStart = 0;
	private int mResume = 0;
	private int mRestart = 0;
	
	private TextView mTvCreate;
	private TextView mTvStart;
	private TextView mTvResume;
	private TextView mTvRestart;	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b);

		// Access the TextView by calling Activity's findViewById()
		// textView1 = (TextView) findViewById(R.id.textView1);
        mTvCreate=(TextView)findViewById(R.id.create);
        mTvStart=(TextView)findViewById(R.id.start);
        mTvResume=(TextView)findViewById(R.id.resume);
        mTvRestart=(TextView)findViewById(R.id.restart);

		Button closeButton = (Button) findViewById(R.id.bClose); 
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Check for previously saved state
		if (savedInstanceState != null) {
			mCreate = savedInstanceState.getInt(CREATE_KEY);
			mStart = savedInstanceState.getInt(START_KEY);
			mResume = savedInstanceState.getInt(RESUME_KEY);
			mRestart = savedInstanceState.getInt(RESTART_KEY);			
		}

		Log.i(TAG, ActivityConstant.ON_CREATE);

		mCreate++;
		displayCounts();
	}


	
	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG, ActivityConstant.ON_START);

		mStart++;
		displayCounts();
	}


	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, ActivityConstant.ON_RESUME);

		mResume++;
		displayCounts();
	}

	
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, ActivityConstant.ON_PAUSE);
	}


	
	@Override
	public void onStop() {
		super.onStop();
		resetCounts();		
		Log.i(TAG, ActivityConstant.ON_STOP);
	}


	
	@Override
	public void onRestart() {
		super.onRestart();
		Log.i(TAG, ActivityConstant.ON_RESTART);
		
		mRestart++;
		displayCounts();
	}

	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, ActivityConstant.ON_DESTROY);
	}

	
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt(CREATE_KEY, mCreate);
		savedInstanceState.putInt(START_KEY, mStart);
		savedInstanceState.putInt(RESUME_KEY, mResume);
		savedInstanceState.putInt(RESTART_KEY, mRestart);
		
		super.onSaveInstanceState(savedInstanceState);
	}


	
	// Updates the displayed counters
	public void displayCounts() {
		mTvCreate.setText("onCreate() calls: " + mCreate);
		mTvStart.setText("onStart() calls: " + mStart);
		mTvResume.setText("onResume() calls: " + mResume);
		mTvRestart.setText("onRestart() calls: " + mRestart);
	}
	
	
	public void resetCounts() {
		mCreate = 0;
		mStart = 0;
		mResume = 0;
		mRestart = 0;		
	}
}
