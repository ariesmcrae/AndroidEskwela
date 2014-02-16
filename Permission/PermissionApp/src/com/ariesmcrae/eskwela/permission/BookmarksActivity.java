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
package com.ariesmcrae.eskwela.permission;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.ariesmcrae.eskwela.permission.R;

/** @author aries@ariesmcrae.com */
public class BookmarksActivity extends Activity {
	
	private static final String TAG = "eskwela-Permissions";

	static final String[] projection = { Browser.BookmarkColumns.TITLE,
			Browser.BookmarkColumns.URL };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks_activity);

		Button getBookmarksButton = (Button) findViewById(R.id.get_bookmarks_button);
		getBookmarksButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadBookmarks();
			}
		});

		Button goToDangerousActivityButton = (Button) findViewById(R.id.go_to_dangerous_activity_button);
		goToDangerousActivityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startGoToDangerousActivity();
			}
		});
	}

	
	
	private void loadBookmarks() {
		Log.i(TAG, "Entered loadBookmarks()");

		String text = "";
		
		Cursor query = getContentResolver().query(Browser.BOOKMARKS_URI, projection, null, null, null);
		query.moveToFirst();
		
		while (query.moveToNext()) {
			text += query.getString(query.getColumnIndex(Browser.BookmarkColumns.TITLE));
			text += "\n";
			text += query.getString(query.getColumnIndex(Browser.BookmarkColumns.URL));
			text += "\n\n";
		}

		TextView box = (TextView) findViewById(R.id.text);
		box.setText(text);

		Log.i(TAG, "Bookmarks loaded");
	}

	
	
	private void startGoToDangerousActivity() {
		Log.i(TAG, "Entered startGoToDangerousActivity()");
		startActivity(new Intent(this, GoToDangerousActivity.class));
	}

}
