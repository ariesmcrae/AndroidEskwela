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
package com.ariesmcrae.eskwela.intent.test;

import com.ariesmcrae.eskwela.intent.ActivityLoaderActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/** @author aries@ariesmcrae.com */
public class ImplicitTest extends
		ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
	private Solo solo;

	public ImplicitTest() {
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

		// Wait for activity: 'com.ariesmcrae.eskwela.intent.ActivityLoaderActivity'
		assertTrue("com.ariesmcrae.eskwela.intent.ActivityLoaderActivity is not found!",
					solo.waitForActivity(com.ariesmcrae.eskwela.intent.ActivityLoaderActivity.class));

		// Click on Implicit Activation
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.intent.R.id.implicit_activation_button));

		// Assert that: Log Message 'Entered startImplicitActivation() is shown
		assertTrue("Log message - 'Entered startImplicitActivation()' is not shown!",
					solo.waitForLogMessage("Entered startImplicitActivation()", timeout));

		// Assert that: 'Chooser Intent Action:android.intent.action.CHOOSER' is shown
		assertTrue(
				"Log message - 'Chooser Intent Action:android.intent.action.CHOOSER' is not shown",
				solo.waitForLogMessage("Chooser Intent Action:android.intent.action.CHOOSER", timeout));

		// Wait for activity: 'com.android.internal.app.ChooserActivity'
		assertTrue("ChooserActivity is not found!", solo.waitForActivity("ChooserActivity"));

		// Click on MockBrowser
		solo.clickInList(2, 0);

	}
}
