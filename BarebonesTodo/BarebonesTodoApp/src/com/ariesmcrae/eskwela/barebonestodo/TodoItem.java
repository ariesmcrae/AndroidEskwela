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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;


/** @author aries@ariesmcrae.com */
public class TodoItem {

	public static final String ITEM_SEP = System.getProperty("line.separator");

	public enum Priority { LOW, MED, HIGH };
	public enum Status { NOTDONE, DONE };

	public final static String TITLE = "title";
	public final static String PRIORITY = "priority";
	public final static String STATUS = "status";
	public final static String DATE = "date";
	public final static String FILENAME = "filename";

	public final static DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

	private String mTitle = new String();
	private Priority mPriority = Priority.LOW;
	private Status mStatus = Status.NOTDONE;
	private Date mDate = new Date();

	TodoItem(String title, Priority priority, Status status, Date date) {
		this.mTitle = title;
		this.mPriority = priority;
		this.mStatus = status;
		this.mDate = date;
	}

	
	// Create a new TodoItem from data packaged in an Intent
	TodoItem(Intent intent) {
		mTitle = intent.getStringExtra(TodoItem.TITLE);
		mPriority = Priority.valueOf(intent.getStringExtra(TodoItem.PRIORITY));
		mStatus = Status.valueOf(intent.getStringExtra(TodoItem.STATUS));

		try {
			mDate = TodoItem.FORMAT.parse(intent.getStringExtra(TodoItem.DATE));
		} catch (ParseException e) {
			mDate = new Date();
		}
	}

	
	
	public String getTitle() { return mTitle; }
	public void setTitle(String title) { mTitle = title;}

	public Priority getPriority() { return mPriority; }
	public void setPriority(Priority priority) { mPriority = priority; }
	
	public Status getStatus() { return mStatus; }
	public void setStatus(Status status) { mStatus = status; }

	public Date getDate() { return mDate; }
	public void setDate(Date date) { mDate = date; }



	public static void packageIntent(Intent intent, String title, Priority priority, 
									 Status status, String date) {
		// Take a set of String data values and package them for transport in an Intent
		intent.putExtra(TodoItem.TITLE, title);
		intent.putExtra(TodoItem.PRIORITY, priority.toString());
		intent.putExtra(TodoItem.STATUS, status.toString());
		intent.putExtra(TodoItem.DATE, date);
	}

	
	
	public String toString() {
		return mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP + FORMAT.format(mDate);
	}

	
	
	public String toLog() {
		return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
				+ ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
				+ FORMAT.format(mDate);
	}

}
