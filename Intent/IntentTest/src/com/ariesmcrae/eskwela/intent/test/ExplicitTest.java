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
public class ExplicitTest extends
		ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
	private Solo solo;

	public ExplicitTest() {
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

		// Click on Explicit Activation
		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.intent.R.id.explicit_activation_button));

		// Wait for activity: 'com.ariesmcrae.eskwela.intent.ExplicitlyLoadedActivity'
		assertTrue("com.ariesmcrae.eskwela.intent.ExplicitlyLoadedActivity is not found!",
					solo.waitForActivity(com.ariesmcrae.eskwela.intent.ExplicitlyLoadedActivity.class));

		solo.hideSoftKeyboard();
		
		// Enter the text: 'test'
		solo.clearEditText((android.widget.EditText) solo.getView(com.ariesmcrae.eskwela.intent.R.id.editText));
		solo.enterText((android.widget.EditText) solo.getView(com.ariesmcrae.eskwela.intent.R.id.editText), "test");

		solo.hideSoftKeyboard();

		solo.clickOnView(solo.getView(com.ariesmcrae.eskwela.intent.R.id.enter_button));

		// Assert that: 'textView1' is shown
		assertTrue("textView1 is not shown!", solo.waitForView(solo.getView(com.ariesmcrae.eskwela.intent.R.id.textView1)));

		// assert that the string 'test' is found on the display
		assertTrue("'test' is not displayed!", solo.searchText("test"));

		// Assert that: Log Message 'Entered startExplicitActivation() is shown
		assertTrue("Log message - 'Entered startExplicitActivation()' is not shown!",
					solo.waitForLogMessage("Entered startExplicitActivation()", timeout));

		// Assert that: Log Message 'Entered enterClicked()' is shown
		assertTrue("Log message - 'Entered enterClicked()' is not shown", solo.waitForLogMessage("Entered enterClicked()", timeout));

		// Assert that: Log Message 'Entered onActivityResult()' is shown
		assertTrue("Log message - 'Entered onActivityResult()", solo.waitForLogMessage("Entered onActivityResult()", timeout));
	}
}
