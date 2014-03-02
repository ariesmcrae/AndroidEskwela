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
package com.ariesmcrae.eskwela.bubbles.test;


import com.ariesmcrae.eskwela.bubbles.BubbleActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

/** @author aries@ariesmcrae.com */
public class BubbleActivityFloatOffScreen extends ActivityInstrumentationTestCase2<BubbleActivity> {
  	private Solo solo;
  	
  	public BubbleActivityFloatOffScreen() {
		super(BubbleActivity.class);
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
		// Wait for activity: 'BubbleActivity'
		solo.waitForActivity(BubbleActivity.class, 2000);

		// Click on action bar item
		solo.clickOnActionBarItem(0x2);
		
		solo.sleep(1000);

		// Click to create a bubble
		solo.clickOnScreen(500, 500);
		
		solo.sleep(5000);
	}
}
