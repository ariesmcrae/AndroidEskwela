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

import com.ariesmcrae.eskwela.permission.ActivityLoaderActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/** @author aries@ariesmcrae.com */
public class TestDangerousApp extends ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
  	private Solo solo;
  	
  	public TestDangerousApp() {
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
		
		// Wait for activity: 'com.ariesmcrae.eskwela.permission.ActivityLoaderActivity'
		assertTrue("com.ariesmcrae.eskwela.permission.ActivityLoaderActivity is not found!", solo.waitForActivity(com.ariesmcrae.eskwela.permission.ActivityLoaderActivity.class));

		// Click on Bookmarks Activity
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.permission.R.id.start_bookmarks_button));

		// Wait for activity: 'com.ariesmcrae.eskwela.permission.BookmarksActivity'
		assertTrue("com.ariesmcrae.eskwela.permission.BookmarksActivity is not found!", solo.waitForActivity(com.ariesmcrae.eskwela.permission.BookmarksActivity.class));

		// Click on Go To DangerousActivity
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.permission.R.id.go_to_dangerous_activity_button));
		

		// Wait for activity: 'com.ariesmcrae.eskwela.permission.GoToDangerousActivity'
		assertTrue("com.ariesmcrae.eskwela.permission.GoToDangerousActivity is not found!", solo.waitForActivity(com.ariesmcrae.eskwela.permission.GoToDangerousActivity.class));
		
		// Assert that: 'This button will load a Dangerous Level activity' is shown
		assertTrue("'This button will load a Dangerous Level activity' is not shown!", solo.waitForText(java.util.regex.Pattern.quote("This button will load a Dangerous Level activity")));
		
		// Click on Start Dangerous Activity
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.permission.R.id.start_dangerous_activity_button));
		
		// Assert Log message created - 'Entered startBookMarksActivity()'
		assertTrue("'Entered startBookMarksActivity()' Log message not found.", solo.waitForLogMessage("Entered startBookMarksActivity()",timeout));

		// Assert Log message created - 'Entered startGoToDangerousActivity()'
		assertTrue("'Entered startGoToDangerousActivity()' Log message not found.", solo.waitForLogMessage("Entered startGoToDangerousActivity()",timeout));
		
		
		// Assert Log message created - 'Entered startDangerousActivity()'
		assertTrue("'Entered startDangerousActivity()' Log message not found.", solo.waitForLogMessage("Entered startDangerousActivity()",timeout));
	}
}
