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
package com.ariesmcrae.eskwela.barebonestodo.test;

import android.test.ActivityInstrumentationTestCase2;

import com.ariesmcrae.eskwela.barebonestodo.AddTodoActivity;
import com.ariesmcrae.eskwela.barebonestodo.R;
import com.ariesmcrae.eskwela.barebonestodo.TodoManagerActivity;
import com.robotium.solo.Solo;

/** @author aries@ariesmcrae.com */
public class TestSubmit extends	ActivityInstrumentationTestCase2<TodoManagerActivity> {
	private Solo solo;

	public TestSubmit() {
		super(TodoManagerActivity.class);
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

		int timeout = 10;

		// Wait for activity: 'TodoManagerActivity'
		assertTrue("TodoManagerActivity is not found!",
				solo.waitForActivity(TodoManagerActivity.class, 2000));

		// Click on action bar item to delete all items
		solo.clickOnActionBarItem(0x1);

		// Click on Add New ToDo Item
		solo.clickOnView(solo.getView(R.id.footerView));

		// Wait for activity: 'AddTodoActivity'
		assertTrue("AddTodoActivity is not found!",	solo.waitForActivity(AddTodoActivity.class));

		solo.hideSoftKeyboard();

		// Enter the text: 't1'
		solo.clearEditText((android.widget.EditText) solo.getView(R.id.title));
		solo.enterText((android.widget.EditText) solo.getView(R.id.title), "t1");

		solo.hideSoftKeyboard();

		// Click on Done:
		solo.clickOnView(solo.getView(R.id.statusDone));
		
		// Click on Low
		solo.clickOnView(solo.getView(R.id.lowPriority));
		
		// Click on Choose Date
		solo.clickOnView(solo.getView(R.id.date_picker_button));

		// Wait for dialog
		solo.waitForDialogToOpen(10000);
		
		// Enter the text: 'Feb'
		solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input"));
		solo.enterText((android.widget.EditText) solo.getView("numberpicker_input"), "Feb");
		
		// Enter the text: '28'
		solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input", 1));
		solo.enterText((android.widget.EditText) solo.getView("numberpicker_input", 1),	"28");
		
		// Enter the text: '2014'
		solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input", 2));
		solo.enterText((android.widget.EditText) solo.getView("numberpicker_input", 2),	"2014");

		// Really set the date
		solo.setDatePicker(0, 2014, 1, 28);

		// Click on Done
		solo.clickOnView(solo.getView(android.R.id.button1));

		// Click on Choose Time
		solo.clickOnView(solo.getView(R.id.time_picker_button));
		
		// Wait for dialog
		solo.waitForDialogToOpen(10000);
		
		// Enter the text: '9'
		solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input"));
		solo.enterText((android.widget.EditText) solo.getView("numberpicker_input"), "9");
		
		// Enter the text: '19'
		solo.clearEditText((android.widget.EditText) solo.getView("numberpicker_input", 1));
		solo.enterText((android.widget.EditText) solo.getView("numberpicker_input", 1),	"19");

		// Really set the time
		solo.setTimePicker(0, 9, 19);

		// Click on Done
		solo.clickOnView(solo.getView(android.R.id.button1));

		// Click on Submit
		solo.clickOnView(solo.getView(R.id.submitButton));

		// Wait for activity: 'TodoManagerActivity'
		assertTrue("TodoManagerActivity is not found!",	solo.waitForActivity(TodoManagerActivity.class, 2000));

		// Click on action bar item to dump items to log
		solo.clickOnActionBarItem(0x2);
		
		assertTrue("Menu didn't close", solo.waitForDialogToClose());
		
		// Wait for Log Message 'Item 0:
		// Title:t1,Priority:LOW,Status:DONE,Date:2014-02-28 09:19:00'
		assertTrue("Log message: 'Title:t1,Priority:LOW,Status:DONE,Date:2014-02-28 09:19:00' not found",
					solo.waitForLogMessage("Title:t1,Priority:LOW,Status:DONE,Date:2014-02-28 09:19:00", timeout));
	}
}
