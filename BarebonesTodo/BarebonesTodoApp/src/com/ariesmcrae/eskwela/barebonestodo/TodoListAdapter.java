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

import java.util.ArrayList;
import java.util.List;

import com.ariesmcrae.eskwela.barebonestodo.R;
import com.ariesmcrae.eskwela.barebonestodo.TodoItem.Status;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** @author aries@ariesmcrae.com */
public class TodoListAdapter extends BaseAdapter {

	private static final String TAG = "eskwela-BarebonesTodo";	
	
	private final List<TodoItem> mItems = new ArrayList<TodoItem>();
	private final Context mContext;

	
	public TodoListAdapter(Context context) {
		mContext = context;
	}

	

	public void add(TodoItem item) {
		// Add a TodoItem to the adapter. Notify observers that the data set has changed
		mItems.add(item);
		notifyDataSetChanged();
	}
	

	
	public void clear(){
		// Clears the list adapter of all items.
		mItems.clear();
		notifyDataSetChanged();
	}



	@Override
	public int getCount() {
		// Returns the number of ToDoItems
		return mItems.size();
	}

	

	@Override
	public Object getItem(int pos) {
		// Retrieve a specific TodoItem from its specific position.
		return mItems.get(pos);
	}

	
	
	@Override
	public long getItemId(int pos) {
		// Get the ID for the TodoItem. In this case it's just the position
		return pos;

	}

	
	
	@Override /** Create a View to display the TodoItem at specified position in mItems  */
	public View getView(int position, View convertView, ViewGroup parent) {
		final TodoItem todoItem = (TodoItem)getItem(position);

		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout itemLayout = (RelativeLayout)inflater.inflate(R.layout.todo_item, null);		
		
		final TextView titleView = (TextView)itemLayout.findViewById(R.id.titleView);
		titleView.setText(todoItem.getTitle());

		final CheckBox statusView = (CheckBox)itemLayout.findViewById(R.id.statusCheckBox);
		statusView.setChecked(todoItem.getStatus() == Status.DONE);
		
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				log("Entered onCheckedChanged()");
				
				if (isChecked) {
					todoItem.setStatus(Status.DONE);
				} else {
					todoItem.setStatus(Status.NOTDONE);
				}
			}
		});

		final TextView priorityView = (TextView)itemLayout.findViewById(R.id.priorityView);;
		priorityView.setText(todoItem.getPriority().toString());
		
		final TextView dateView = (TextView)itemLayout.findViewById(R.id.dateView);;
		dateView.setText(TodoItem.FORMAT.format(todoItem.getDate()));

		return itemLayout;
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
