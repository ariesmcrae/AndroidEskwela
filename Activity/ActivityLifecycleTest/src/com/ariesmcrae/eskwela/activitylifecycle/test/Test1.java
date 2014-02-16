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
package com.ariesmcrae.eskwela.activitylifecycle.test;

import com.ariesmcrae.eskwela.activitylifecycle.ActivityA;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


public class Test1 extends ActivityInstrumentationTestCase2<ActivityA> {
  	private Solo solo;
  	
  	public Test1() {
		super(ActivityA.class);
  	}

  	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
		assertTrue("Activity stack not empty.", solo.waitForEmptyActivityStack(5000));
  	}
  
	public void testRun() {
		// Wait for activity: ' com.ariesmcrae.eskwela.activitylifecycle.ActivityA'
		assertTrue("course.labs.activitylab.ActivityOne is not found!", solo.waitForActivity(com.ariesmcrae.eskwela.activitylifecycle.ActivityA.class));
		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 1"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 1"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 1"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 0"));

		// Rotate the screen
		solo.setActivityOrientation(Solo.LANDSCAPE);
		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 2"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 2"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 2"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 0"));
		
		// Rotate the screen
		solo.setActivityOrientation(Solo.PORTRAIT);
		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 3"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 3"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 3"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 0"));


	}
}
