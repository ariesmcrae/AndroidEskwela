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
package com.ariesmcrae.eskwela.celebtweet.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import com.ariesmcrae.eskwela.celebtweet.MainActivity;
import com.ariesmcrae.eskwela.celebtweet.R;
import com.ariesmcrae.eskwela.celebtweet.TestFrontEndActivity;

/** @author aries@ariesmcrae.com */
public class NewFeedTest extends ActivityInstrumentationTestCase2<TestFrontEndActivity> {
	private Solo solo;

	public NewFeedTest() {
		super(TestFrontEndActivity.class);
	}

	
	
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation());
		getActivity();
	}

	
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	
	
	public void testRun() {
		int shortDelay = 2000;

		// Wait for activity: 'TestFrontEndActivity'
		solo.waitForActivity(TestFrontEndActivity.class, 2000);

		solo.sleep(shortDelay);

		// Click on Make Tweets New
		solo.clickOnView(solo.getView(R.id.rejuv_tweets_button));

		solo.sleep(shortDelay);

		// Click on Start Main Activty
		solo.clickOnView(solo.getView(R.id.start_main_button));

		// Wait for activity: 'MainActivity'
		assertTrue("MainActivity is not found!", solo.waitForActivity(MainActivity.class));

		solo.sleep(shortDelay);

		// Click on taylorswift13
		solo.clickOnView(solo.getView(android.R.id.text1));

		// Assert that: 'feed_view' is shown
		assertTrue("feed_view not shown!", solo.waitForView(solo.getView(R.id.feed_view)));

		// Assert that: 'Taylor Swift' is shown
		assertTrue("'Taylor Swift' is not shown!", solo.searchText("Taylor Swift"));

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		solo.sleep(shortDelay);

		// Click on msrebeccablack
		solo.clickOnView(solo.getView(android.R.id.text1, 1));

		// Assert that: 'feed_view' is shown
		assertTrue("'Rebecca Black' is not shown!", solo.waitForView(solo.getView(R.id.feed_view)));

		// Assert that: 'feed_view' is shown
		assertTrue("'Rebecca Black' is not shown!", solo.searchText("Rebecca Black"));

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		// Click on ladygaga
		solo.clickOnView(solo.getView(android.R.id.text1, 2));

		// Assert that: 'feed_view' is shown
		assertTrue("'feed_view' is not shown!", solo.waitForView(solo.getView(R.id.feed_view)));

		// Assert that: 'Lady Gaga' is shown
		assertTrue("'Lady Gaga' is not shown!", solo.searchText("Lady Gaga"));
	}
}
