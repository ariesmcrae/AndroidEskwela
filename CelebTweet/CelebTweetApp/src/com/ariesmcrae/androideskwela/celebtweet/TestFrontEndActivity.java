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

/** @author aries@ariesmcrae.com */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.ariesmcrae.androideskwela.celebtweet.R;

public class TestFrontEndActivity extends Activity {

	private final static long DAWN_OF_TIME = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_front_end);

		Button ageTweetsButton = (Button) findViewById(R.id.age_tweets_button);
		ageTweetsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFileAge(DAWN_OF_TIME);
			}

		});

		Button rejuvTweetsButton = (Button) findViewById(R.id.rejuv_tweets_button);
		rejuvTweetsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFileAge(System.currentTimeMillis());
			}

		});

		Button startMainActivityButton = (Button) findViewById(R.id.start_main_button);
		startMainActivityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(TestFrontEndActivity.this, MainActivity.class));
			}
		});

		createTweetFileIfMissing();
	}

	
	
	
	private void createTweetFileIfMissing() {
		String fname = TestFrontEndActivity.this.getFilesDir() + "/" + MainActivity.TWEET_FILENAME;

		File file = new File(fname);
		if (!file.exists()) {

			PrintWriter out = null;
			BufferedReader in = null;

			try {
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(openFileOutput(MainActivity.TWEET_FILENAME,
						Context.MODE_PRIVATE))));

				for (int resId : DownloaderTask.txtFeeds) {
					in = new BufferedReader(new InputStreamReader(getResources().openRawResource(resId)));

					String line;
					StringBuffer buffer = new StringBuffer();

					while ((line = in.readLine()) != null) {
						buffer.append(line);
					}

					out.println(buffer);

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Resources.NotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != in) {
						in.close();
					}
					if (null != out) {
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	
	private void setFileAge(long timestamp) {
		String fname = TestFrontEndActivity.this.getFilesDir() + "/" + MainActivity.TWEET_FILENAME;
		File file = new File(fname);
		if (file.exists()) {
			file.setLastModified(timestamp);
		}
	}

}
