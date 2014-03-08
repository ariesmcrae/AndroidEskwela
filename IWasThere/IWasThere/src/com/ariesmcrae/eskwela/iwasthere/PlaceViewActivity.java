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

import android.app.ListActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.ariesmcrae.eskwela.iwasthere.R;

/** @author aries@ariesmcrae.com */
public class PlaceViewActivity extends ListActivity implements LocationListener {
	private static final long FIVE_MINS = 5 * 60 * 1000;

	private static String TAG = "eskwela-IWasThere";

	// The last valid location reading
	private Location mLastLocationReading;

	// The ListView's adapter
	private PlaceViewAdapter mAdapter;

	// default minimum time between new location readings
	private long mMinTime = 5000;

	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;

	// Reference to the LocationManager
	private LocationManager mLocationManager;

	// A fake location provider used for testing
	private MockLocationProvider mMockLocationProvider;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO 1 - Set up the app's user interface
		// This class is a ListActivity, so it has its own ListView
		// ListView's adapter should be a PlaceViewAdapter

		// TODO 2- add a footerView to the ListView
		// You can use footer_view.xml to define the footer

		// When the footerView's onClick() method is called, it must issue the follow log call
		// log("Entered footerView.OnClickListener.onClick()");

		// footerView must respond to user clicks. Must handle 3 cases:
		// 1) The current location is new - download new Place Badge. Issue the
		// following log call: log("Starting Place Download");

		// 2) The current location has been seen before - issue Toast message.
		// Issue the following log call: log("You already have this location badge");

		// 3) There is no current location - response is up to you. 
		// The best solution is to disable the footerView until you have a location.
		// Issue the following log call:
		// log("Location data is not available");
		
		//FIXME acquire location readings from Android.
		//listen for location updates from the NETWORK_PROVIDER
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();

		mMockLocationProvider = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);

		// TODO 3 - Check NETWORK_PROVIDER for an existing location reading.
		// Only keep this last reading if it is fresh - less than 5 minutes old.

		// TODO 4 - register to receive location updates from NETWORK_PROVIDER
	}

	
	
	@Override
	protected void onPause() {
		mMockLocationProvider.shutdown();

		// TODO 5 - unregister for location updates

		super.onPause();
	}

	
	
	public void addNewPlace(PlaceRecord place) {
		log("Entered addNewPlace()");
		mAdapter.add(place);
	}

	
	
	@Override
	public void onLocationChanged(Location currentLocation) {
		// TODO 6 - Handle location updates
		// Cases to consider
		// 1) If there is no mLastLocationReading, keep currentLocation.
		// 2) If the currentLocation is older than the mLastLocationReading, ignore currentLocation
		// 3) If the currentLocation is newer than the last locations, keep the currentLocation.
	}
	
	

	@Override
	public void onProviderDisabled(String provider) {
		// not implemented
	}

	
	
	@Override
	public void onProviderEnabled(String provider) {
		// not implemented
	}

	
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// not implemented
	}

	
	
	private long age(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
			case R.id.print_badges:
				ArrayList<PlaceRecord> currData = mAdapter.getList();
			
				for (int i = 0; i < currData.size(); i++) {
					log(currData.get(i).toString());
				}
				
				return true;
				
			case R.id.delete_badges:
				mAdapter.removeAllViews();
				return true;
				
			case R.id.place_one:
				mMockLocationProvider.pushLocation(37.422, -122.084);
				return true;
				
			case R.id.place_invalid:
				mMockLocationProvider.pushLocation(0, 0);
				return true;
				
			case R.id.place_two:
				mMockLocationProvider.pushLocation(38.996667, -76.9275);
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	
	
	private static void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Log.i(TAG, msg);
	}

}
