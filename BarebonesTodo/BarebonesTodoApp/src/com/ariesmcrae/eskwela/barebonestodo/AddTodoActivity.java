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
package com.ariesmcrae.eskwela.barebonestodo;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import com.ariesmcrae.eskwela.barebonestodo.R;
import com.ariesmcrae.eskwela.barebonestodo.TodoItem.Priority;
import com.ariesmcrae.eskwela.barebonestodo.TodoItem.Status;

/** @author aries@ariesmcrae.com */
public class AddTodoActivity extends Activity {

	private static final String TAG = "eskwela-BarebonesTodo";	

	// 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
	private static final int SEVEN_DAYS = 604800000;

	private static String timeString;
	private static String dateString;
	private static TextView dateView;
	private static TextView timeView;

	
	private Date mDate;
	private RadioGroup mPriorityRadioGroup;
	private RadioGroup mStatusRadioGroup;
	private EditText mTitleText;
	private RadioButton mDefaultStatusButton;
	private RadioButton mDefaultPriorityButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_todo);

		mTitleText = (EditText) findViewById(R.id.title);
		mDefaultStatusButton = (RadioButton) findViewById(R.id.statusNotDone);
		mDefaultPriorityButton = (RadioButton) findViewById(R.id.medPriority);
		mPriorityRadioGroup = (RadioGroup) findViewById(R.id.priorityGroup);
		mStatusRadioGroup = (RadioGroup) findViewById(R.id.statusGroup);
		dateView = (TextView) findViewById(R.id.date);
		timeView = (TextView) findViewById(R.id.time);

		setDefaultDateTime();

		// OnClickListener for the Date button, calls showDatePickerDialog() to 
		// Show the Date dialog
		final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
		datePickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		
		// OnClickListener for the Time button, calls showTimePickerDialog() to 
		// show the Time Dialog
		final Button timePickerButton = (Button) findViewById(R.id.time_picker_button);
		timePickerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePickerDialog();
			}
		});

		
		// OnClickListener for the Cancel Button, 
		final Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered cancelButton.OnClickListener.onClick()");

				setResult(RESULT_CANCELED, null);					
				finish();
			}
		});

		
		//OnClickListener for the Reset Button
		final Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered resetButton.OnClickListener.onClick()");

				mTitleText.setText(null);
				mStatusRadioGroup.check(mDefaultStatusButton.getId());
				mPriorityRadioGroup.check(mDefaultPriorityButton.getId());
				setDefaultDateTime();
			}
		});


		// OnClickListener for the Submit Button Implement onClick().
		final Button submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered submitButton.OnClickListener.onClick()");
				
				String titleString = mTitleText.getText().toString();
				Status status = getStatus();
				Priority priority = getPriority();
				String fullDate = dateString + " " + timeString;

				// Package TodoItem data into an Intent
				Intent data = new Intent();
				TodoItem.packageIntent(data, titleString, priority, status, fullDate);

				setResult(RESULT_OK, data);
				finish();
			}
		});
	}


	
	private void setDefaultDateTime() {
		// Default is current time + 7 days
		mDate = new Date();
		mDate = new Date(mDate.getTime() + SEVEN_DAYS);

		Calendar c = Calendar.getInstance();
		c.setTime(mDate);

		setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

		dateView.setText(dateString);

		setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.MILLISECOND));

		timeView.setText(timeString);
	}

	
	
	private static void setDateString(int year, int monthOfYear, int dayOfMonth) {
		// Increment monthOfYear for Calendar/Date -> Time Format setting
		monthOfYear++;
		String mon = "" + monthOfYear;
		String day = "" + dayOfMonth;

		if (monthOfYear < 10)
			mon = "0" + monthOfYear;
		if (dayOfMonth < 10)
			day = "0" + dayOfMonth;

		dateString = year + "-" + mon + "-" + day;
	}

	
	
	private static void setTimeString(int hourOfDay, int minute, int mili) {
		String hour = "" + hourOfDay;
		String min = "" + minute;

		if (hourOfDay < 10)
			hour = "0" + hourOfDay;
		if (minute < 10)
			min = "0" + minute;

		timeString = hour + ":" + min + ":00";
	}

	
	private Priority getPriority() {
		switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
			case R.id.lowPriority: return Priority.LOW;
			case R.id.highPriority: return Priority.HIGH;
			default: return Priority.MED;
		}
	}

	
	
	private Status getStatus() {
		switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
			case R.id.statusDone: {
				return Status.DONE;
			} default: {
				return Status.NOTDONE;
			}
		}
	}

	

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		// Used to pick a TodoItem deadline date
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker

			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			setDateString(year, monthOfYear, dayOfMonth);
			dateView.setText(dateString);
		}
	}



	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		// Used to pick a TodoItem deadline time
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			setTimeString(hourOfDay, minute, 0);
			timeView.setText(timeString);
		}
	}

	
	
	private void showDatePickerDialog() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}

	
	
	private void showTimePickerDialog() {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	
	
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Log.i(TAG, msg);
	}

}
