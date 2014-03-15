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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ariesmcrae.eskwela.iwasthere.R;
import com.ariesmcrae.eskwela.iwasthere.contentprovider.PlaceBadgesContract;

/** @author aries@ariesmcrae.com */
public class PlaceViewAdapter extends CursorAdapter {

	private static final String APP_DIR = "IWasThere/Badges";
	private ArrayList<PlaceRecord> list = new ArrayList<PlaceRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	private String mBitmapStoragePath;

	
	
	public PlaceViewAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);
		
		mContext = context;
		inflater = LayoutInflater.from(mContext);

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			try {

				String root = mContext.getExternalFilesDir(null).getCanonicalPath();

				if (null != root) {
					File bitmapStorageDir = new File(root, APP_DIR);
					bitmapStorageDir.mkdirs();
					mBitmapStoragePath = bitmapStorageDir.getCanonicalPath();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	@Override
	public Cursor swapCursor(Cursor newCursor) {
		super.swapCursor(newCursor);

		if (newCursor != null) {
		    list.clear(); //cleared before we start adding new PlaceRecords into it.
		    
		    while (newCursor.moveToNext()) {
		        list.add(getPlaceRecordFromCursor(newCursor)); // add the current place to the list
		    }
		}   
		
		return newCursor;
	}

	
	
	// Returns a new PlaceRecord for the data at the cursor's current position
	private PlaceRecord getPlaceRecordFromCursor(Cursor cursor) {
		String flagBitmapPath = cursor.getString(cursor.getColumnIndex(PlaceBadgesContract.FLAG_BITMAP_PATH));
		String countryName = cursor.getString(cursor.getColumnIndex(PlaceBadgesContract.COUNTRY_NAME));
		String placeName = cursor.getString(cursor.getColumnIndex(PlaceBadgesContract.PLACE_NAME));
		double lat = cursor.getDouble(cursor.getColumnIndex(PlaceBadgesContract.LAT));
		double lon = cursor.getDouble(cursor.getColumnIndex(PlaceBadgesContract.LON));

		return new PlaceRecord(null, flagBitmapPath, countryName, placeName, lat, lon);
	}

	
	
	public int getCount() {
		return list.size();
	}

	
	
	public Object getItem(int position) {
		return list.get(position);
	}

	
	
	public long getItemId(int position) {
		return position;
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
		String lastPathSegment = Uri.parse(listItem.getFlagUrl()).getLastPathSegment();
		String filePath = mBitmapStoragePath + "/" + lastPathSegment;

		if (storeBitmapToFile(listItem.getFlagBitmap(), filePath)) {

			listItem.setFlagBitmapPath(filePath);
			list.add(listItem);

			// Use ContentResolver to insert new record into the ContentProvider. 
            ContentValues values = new ContentValues();
            values.put(PlaceBadgesContract.FLAG_BITMAP_PATH, listItem.getFlagBitmapPath());
            values.put(PlaceBadgesContract.COUNTRY_NAME, listItem.getCountryName());
            values.put(PlaceBadgesContract.PLACE_NAME, listItem.getPlace());
            values.put(PlaceBadgesContract.LAT, listItem.getLat());
            values.put(PlaceBadgesContract.LON, listItem.getLon());

            mContext.getContentResolver().insert(PlaceBadgesContract.CONTENT_URI, values);			
        }
	}
	
	
	

	public ArrayList<PlaceRecord> getList() {
		return list;
	}


	
	public void removeAllViews() {
		list.clear();

		mContext.getContentResolver().delete(PlaceBadgesContract.CONTENT_URI, null, null);
	}

	
	
	
	// When the ListView needs views to display, it will first call this method (newView) to get a brand new view. 
	// It will then call bindView() to add data to that view.
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();

		holder.flag.setImageBitmap(getBitmapFromFile(cursor.getString(cursor.getColumnIndex(PlaceBadgesContract.FLAG_BITMAP_PATH))));
		holder.country.setText("Country: " + cursor.getString(cursor.getColumnIndex(PlaceBadgesContract.COUNTRY_NAME)));
		holder.place.setText("Place: "  + cursor.getString(cursor.getColumnIndex(PlaceBadgesContract.PLACE_NAME)));
	}

	
	
	// When the ListView needs views to display, it will first call this method (newView) to get a brand new view. 
	// It will then call bindView() to add data to that view.
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View newView;
		ViewHolder holder = new ViewHolder();

		newView = inflater.inflate(R.layout.place_badge_view, null);
		holder.flag = (ImageView) newView.findViewById(R.id.flag);
		holder.country = (TextView) newView.findViewById(R.id.country_name);
		holder.place = (TextView) newView.findViewById(R.id.place_name);

		newView.setTag(holder);

		return newView;
	}

	
	
	
	private Bitmap getBitmapFromFile(String filePath) {
		return BitmapFactory.decodeFile(filePath);
	}
	
	
	

	private boolean storeBitmapToFile(Bitmap bitmap, String filePath) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));

				bitmap.compress(CompressFormat.PNG, 100, bos);

				bos.flush();
				bos.close();

			} catch (FileNotFoundException e) {
				return false;
				
			} catch (IOException e) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
}
