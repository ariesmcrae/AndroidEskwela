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
package com.ariesmcrae.eskwela.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.ariesmcrae.eskwela.permission.R;

/** @author aries@ariesmcrae.com */
public class GoToDangerousActivity extends Activity {
	
	private static final String TAG = "eskwela-Permissions";

	private static final String DANGEROUS_ACTIVITY_ACTION = "com.ariesmcrae.eskwela.permission.DANGEROUS_ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.go_to_dangerous_activity);

		Button startDangerousActivityButton = (Button) findViewById(R.id.start_dangerous_activity_button);
		startDangerousActivityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startDangerousActivity();
			}
		});
	}

	
	
	private void startDangerousActivity() {
		Log.i(TAG, "Entered startDangerousActivity()");
		startActivity(new Intent(DANGEROUS_ACTIVITY_ACTION));
	}

}
