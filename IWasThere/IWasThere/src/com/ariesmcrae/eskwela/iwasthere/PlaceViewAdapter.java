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
package com.ariesmcrae.eskwela.iwasthere;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ariesmcrae.eskwela.iwasthere.R;

/** @author aries@ariesmcrae.com */
public class PlaceViewAdapter extends BaseAdapter {

	private ArrayList<PlaceRecord> list = new ArrayList<PlaceRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	
	
	public PlaceViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	
	
	public int getCount() {
		return list.size();
	}

	
	
	public Object getItem(int position) {
		return position;
	}

	
	
	public long getItemId(int position) {
		return position;
	}

	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView = convertView;
		ViewHolder holder;

		PlaceRecord curr = list.get(position);

		if (convertView == null) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.place_badge_view, null);
			holder.flag = (ImageView) newView.findViewById(R.id.flag);
			holder.country = (TextView) newView.findViewById(R.id.country_name);
			holder.place = (TextView) newView.findViewById(R.id.place_name);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.flag.setImageBitmap(curr.getFlagBitmap());
		holder.country.setText("Country: " + curr.getCountryName());
		holder.place.setText("Place: " + curr.getPlace());

		return newView;
	}

	
	
	static class ViewHolder {
		ImageView flag;
		TextView country;
		TextView place;

	}
	
	

	public boolean intersects(Location location) {
		for (PlaceRecord item : list) {
			if (item.intersects(location)) {
				return true;
			}
		}
		return false;
	}

	
	
	public void add(PlaceRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	
	
	public ArrayList<PlaceRecord> getList() {
		return list;
	}

	
	
	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}
}
