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
package com.ariesmcrae.eskwela.intent;

import com.ariesmcrae.eskwela.intent.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/** @author aries@ariesmcrae.com */
public class ActivityLoaderActivity extends Activity {
	
	private static final int GET_TEXT_REQUEST_CODE = 1;
	private static final String URL = "http://www.google.com";
	private static final String TAG = "eskwela-Intents";
	
	static private final String CHOOSER_TEXT = "Load " + URL + " with:";

	private TextView mUserTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loader_activity);
		
		mUserTextView = (TextView) findViewById(R.id.textView1);

		// Explicit Activation button
		Button explicitActivationButton = (Button) findViewById(R.id.explicit_activation_button);
		explicitActivationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startExplicitActivation();
			}
		});

		// Implicit Activation button
		Button implicitActivationButton = (Button) findViewById(R.id.implicit_activation_button);
		implicitActivationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startImplicitActivation();
			}
		});
	}

	

	private void startExplicitActivation() {
		Log.i(TAG,"Entered startExplicitActivation()");
		startActivityForResult(new Intent(this, ExplicitlyLoadedActivity.class), GET_TEXT_REQUEST_CODE);
	}


	
	// Start a Browser Activity to view a web page or its URL
	private void startImplicitActivation() {
		Log.i(TAG, "Entered startImplicitActivation()");

		Uri webpage = Uri.parse(URL);
		Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
		
		Intent chooserIntent = Intent.createChooser(webIntent, CHOOSER_TEXT);

		Log.i(TAG,"Chooser Intent Action:" + chooserIntent.getAction());
		startActivity(chooserIntent);
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "Entered onActivityResult()");
		
	    if (requestCode == GET_TEXT_REQUEST_CODE && resultCode == RESULT_OK) {
	    	mUserTextView.setText(data.getStringExtra(ExplicitlyLoadedActivity.EXTRA_MESSAGE));
	    }		
	}
}
