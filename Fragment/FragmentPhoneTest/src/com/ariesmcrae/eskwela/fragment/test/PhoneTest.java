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
package com.ariesmcrae.eskwela.fragment.test;

import com.ariesmcrae.eskwela.fragment.MainActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/** @author aries@ariesmcrae.com */
public class PhoneTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;

	public PhoneTest() {
		super(MainActivity.class);
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

		int timeout = 5; 
		
		// Wait for activity: 'com.ariesmcrae.eskwela.fragment.MainActivity'
		assertTrue("MainActivity not found", solo.waitForActivity(com.ariesmcrae.eskwela.fragment.MainActivity.class, 2000));
		
		// Wait for view: 'android.R.id.text1'
		assertTrue("text1 not found", solo.waitForView(android.R.id.text1));

		// Click on ladygaga
		solo.clickOnView(solo.getView(android.R.id.text1));

		assertTrue("feed_view not found", solo.waitForView(solo.getView(com.ariesmcrae.eskwela.fragment.R.id.feed_view)));

		// Assert that: 'the audience cheering!' is shown
		assertTrue("'the audience cheering!' is not shown!", solo.searchText("the audience cheering!"));

		// Wait for onActivityCreated() Log Message:
		assertTrue("onActivityCreated() Log Message not found", solo.waitForLogMessage("Entered onActivityCreated()",timeout));

		// Wait for onItemSelected(0) Log Message:
		assertTrue("onItemSelected(0) Log Message not found", solo.waitForLogMessage("Entered onItemSelected(0)",timeout));

		// Wait for updateFeedDisplay() Log Message:
		assertTrue("updateFeedDisplay() Log Message not found", solo.waitForLogMessage("Entered updateFeedDisplay()",timeout));

		// Clear log
		solo.clearLog();
		
		// Press menu back key
		solo.goBack();
		
		// Wait for view: 'android.R.id.text1'
		assertTrue("text1 not found", solo.waitForView(android.R.id.text1));
		
		// Click on msrebeccablack
		solo.clickOnView(solo.getView(android.R.id.text1, 1));

		// Assert that: feed_view is shown
		assertTrue("feed_view! is not shown!", solo.waitForView(solo.getView(com.ariesmcrae.eskwela.fragment.R.id.feed_view)));

		// Assert that: 'save me from school' is shown
		assertTrue("'save me from school' is not shown!", solo.searchText("save me from school"));

		// Wait for onActivityCreated() Log Message:
		assertTrue("onActivityCreated() Log Message not found", solo.waitForLogMessage("Entered onActivityCreated()",timeout));

		// Wait for Log Message:
		assertTrue("onItemSelected(1) Log Message not found", solo.waitForLogMessage("Entered onItemSelected(1)",timeout));

		// Wait for updateFeedDisplay() Log Message:
		assertTrue("updateFeedDisplay() Log Message not found", solo.waitForLogMessage("Entered updateFeedDisplay()",timeout));

		// Clear log
		solo.clearLog();

		// Press menu back key
		solo.goBack();

		// Click on taylorswift13
		solo.clickOnView(solo.getView(android.R.id.text1, 2));

		// Assert that: feed_view shown
		assertTrue("feed_view not shown", solo.waitForView(solo.getView(com.ariesmcrae.eskwela.fragment.R.id.feed_view)));
		
		// Assert that: 'I love you guys so much' is shown
		assertTrue("'I love you guys so much' is not shown!", solo.searchText("I love you guys so much"));
		
		// Wait for onActivityCreated() Log Message:
		assertTrue("onActivityCreated() Log Message not found", solo.waitForLogMessage("Entered onActivityCreated()",timeout));

		// Wait for onItemSelected(2) Log Message:
		assertTrue("onItemSelected(2) Log Message not found", solo.waitForLogMessage("Entered onItemSelected(2)",timeout));

		// Wait for updateFeedDisplay() Log Message:
		assertTrue("updateFeedDisplay() Log Message not found", solo.waitForLogMessage("Entered updateFeedDisplay()",timeout));
	}
}
