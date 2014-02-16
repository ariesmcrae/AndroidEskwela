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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.ariesmcrae.eskwela.barebonestodo.R;
import com.ariesmcrae.eskwela.barebonestodo.TodoItem.Priority;
import com.ariesmcrae.eskwela.barebonestodo.TodoItem.Status;

/** @author aries@ariesmcrae.com */
public class TodoManagerActivity extends ListActivity {

	private static final String TAG = "eskwela-BarebonesTodo";	
	
	private static final int ADD_TODO_ITEM_REQUEST = 0;

	private static final String FILE_NAME = "TodoManagerActivityData.txt";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

	TodoListAdapter mAdapter;

	 @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			ListView listView = getListView();
			listView.setFooterDividersEnabled(true); // Puts a divider between ToDoItems and FooterView

			TextView footerView = (TextView)getLayoutInflater().inflate(R.layout.footer_view, null); //Loads up TextView from footer_view.xml 
			footerView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					log("Entered footerView.OnClickListener.onClick()");
					//WFT. When do I use 'ThisClassName.this', getBaseContext, getApplicationContext when instantiating Intent?
					startActivityForResult(new Intent(TodoManagerActivity.this, AddTodoActivity.class), ADD_TODO_ITEM_REQUEST);
				}
			});

			listView.addFooterView(footerView);		
			mAdapter = new TodoListAdapter(getApplicationContext());
			listView.setAdapter(mAdapter);
		}

	 
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		log("Entered onActivityResult()");
		
	    if (requestCode == ADD_TODO_ITEM_REQUEST && resultCode == RESULT_OK) {
	    	mAdapter.add(new TodoItem(data));	    	
	    }		
	}

	
	
	@Override
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary
		if (mAdapter.getCount() == 0) {
			loadItems();
		}
	}

	
	
	@Override
	protected void onPause() {
		super.onPause();

		saveItems();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		
		return true;
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_DELETE:
				mAdapter.clear();
				return true;
			case MENU_DUMP:
				dump();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	
	private void dump() {
		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((TodoItem) mAdapter.getItem(i)).toLog();
			log("Item " + i + ": " + data.replace(TodoItem.ITEM_SEP, ","));
		}
	}


	
	private void loadItems() {
		BufferedReader reader = null;
		
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(fis));

			String title = null;
			String priority = null;
			String status = null;
			Date date = null;

			while (null != (title = reader.readLine())) {
				priority = reader.readLine();
				status = reader.readLine();
				date = TodoItem.FORMAT.parse(reader.readLine());
				mAdapter.add(new TodoItem(title, Priority.valueOf(priority), Status.valueOf(status), date));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	
	private void saveItems() {
		PrintWriter writer = null;

		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));

			for (int idx = 0; idx < mAdapter.getCount(); idx++) {
				writer.println(mAdapter.getItem(idx));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
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