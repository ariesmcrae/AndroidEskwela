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
package com.ariesmcrae.eskwela.iwasther.test;

import com.ariesmcrae.eskwela.iwasthere.PlaceViewActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/** @author aries@ariesmcrae.com */
public class TestTwoValidLocations extends
		ActivityInstrumentationTestCase2<PlaceViewActivity> {
	private Solo solo;

	public TestTwoValidLocations() {
		super(PlaceViewActivity.class);
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
		// Wait for activity: 'com.ariesmcrae.eskwela.iwasthere.PlaceViewActivity'
		solo.waitForActivity(com.ariesmcrae.eskwela.iwasthere.PlaceViewActivity.class,
				2000);

		// Set default small timeout to 10887 milliseconds
		Timeout.setSmallTimeout(10887);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.place_one);

		solo.sleep(2000);

		// Click on Get New Place
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.iwasthere.R.id.footer));

		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.place_two);

		solo.sleep(2000);

		// Click on Get New Place
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.iwasthere.R.id.footer));

		solo.sleep(5000);

		// Click on action bar item
		solo.clickOnActionBarItem(com.ariesmcrae.eskwela.iwasthere.R.id.print_badges);

	}
}
