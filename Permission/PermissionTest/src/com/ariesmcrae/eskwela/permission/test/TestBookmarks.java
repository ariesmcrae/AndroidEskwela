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
package com.ariesmcrae.eskwela.permission.test;

import android.test.ActivityInstrumentationTestCase2;

import com.ariesmcrae.eskwela.permission.ActivityLoaderActivity;
import com.robotium.solo.Solo;

/** @author aries@ariesmcrae.com */
public class TestBookmarks extends ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
	private Solo solo;

	public TestBookmarks() {
		super(ActivityLoaderActivity.class);
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
		
		// Wait for activity:
		// 'com.ariesmcrae.eskwela.permission.ActivityLoaderActivity'
		assertTrue("com.ariesmcrae.eskwela.permission.ActivityLoaderActivity is not found!", solo.waitForActivity(com.ariesmcrae.eskwela.permission.ActivityLoaderActivity.class));

		// Click on Bookmarks Activity
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.permission.R.id.start_bookmarks_button));

		// Wait for activity: 'com.ariesmcrae.eskwela.permission.BookmarksActivity'
		assertTrue("com.ariesmcrae.eskwela.permission.BookmarksActivity is not found!", solo.waitForActivity(com.ariesmcrae.eskwela.permission.BookmarksActivity.class));

		// Click on Get Bookmarks
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.permission.R.id.get_bookmarks_button));

		// Check for at least one bookmark
		assertTrue("'www.google.com' is not displayed!", solo.waitForText("http"));

		// Assert Log message created - 'Entered startBookMarksActivity()'
		assertTrue("'Entered startBookMarksActivity()' Log message not found.", solo.waitForLogMessage("Entered startBookMarksActivity()",timeout));

		// Assert Log message created - 'Entered loadBookmarks()'
		assertTrue("'Entered loadBookmarks()' Log message not found.", solo.waitForLogMessage("Entered loadBookmarks()",timeout));

		// Assert Log message created - 'Bookmarks loaded'
		assertTrue("'Bookmarks loaded' Log message not found.", solo.waitForLogMessage("Bookmarks loaded",timeout));

	}
}
