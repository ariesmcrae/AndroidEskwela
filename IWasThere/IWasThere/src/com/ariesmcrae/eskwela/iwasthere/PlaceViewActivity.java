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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
import android.widget.TextView;
import android.widget.Toast;

/** @author aries@ariesmcrae.com */
public class PlaceViewActivity extends ListActivity implements LocationListener {
	private static final long FIVE_MINS = 5 * 60 * 1000;

	private static String TAG = "eskwela-IWasThere";

	// The last valid location reading
	private Location mLastLocationReading;

	// The ListView's adapter
	private PlaceViewAdapter mAdapter;

	// Reference to the LocationManager
	private LocationManager mLocationManager;

	// A fake location provider used for testing
	private MockLocationProvider mMockLocationProvider;
	
	// default minimum time between new location readings
	private long mMinTime = 5000; //5 seconds

	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f; //1000 meters
	
	private TextView mFooterView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListView listView = getListView();
		listView.setFooterDividersEnabled(true); // Puts a divider between ToDoItems and FooterView		

		mFooterView = (TextView)getLayoutInflater().inflate(R.layout.footer_view, null); //Loads up TextView from footer_view.xml 
		listView.addFooterView(mFooterView);

		toggleFooter(true);

		mAdapter = new PlaceViewAdapter(getApplicationContext());
		listView.setAdapter(mAdapter);		
		
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				log("Entered footerView.OnClickListener.onClick()");
				updateView();
			}
		});
	}

	
	
	
	private void updateView() {
		if (mLastLocationReading == null) {
			log("Location data is not available");
			return;
		}
		
		boolean iHaveBeenHereBefore = mAdapter.intersects(mLastLocationReading);
		
		if (iHaveBeenHereBefore) {
			String message = "You already have this location badge";
			log(message);
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();			
		} else {
			log("Starting Place Download");
			
			//This is an AsyncTask. It performs work outside the main thread.
			new PlaceDownloaderTask(this).execute(mLastLocationReading);
		}
	}
	
	
	
	
	
	
	@Override
	protected void onResume() {
		super.onResume();

		// Acquire reference to the LocationManager
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (mLocationManager == null) {
			finish();			
		}		
		
		// Get best last location measurement
		mLastLocationReading = bestLastKnownLocation(mMinDistance, FIVE_MINS);			
		
		mMockLocationProvider = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);
		
		// Determine whether initial reading is "good enough"
		if (mLastLocationReading != null && age(mLastLocationReading) < FIVE_MINS) {
			toggleFooter(false);
			// Register for network location updates
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance, this);

			// Register for GPS location updates
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mMinTime, mMinDistance, this);

			// Schedule a runnable to unregister location listeners
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
				@Override
				public void run() {
					mLocationManager.removeUpdates(PlaceViewActivity.this);
				}
			}, FIVE_MINS, TimeUnit.MILLISECONDS); 	// Unregister location listeners after 5 mins
		}		
	}

	
	
	@Override
	protected void onPause() {
		mMockLocationProvider.shutdown();

		mLocationManager.removeUpdates(this);		

		super.onPause();
	}

	
	
	public void addNewPlace(PlaceRecord place) {
		log("Entered addNewPlace()");
		mAdapter.add(place);
	}

	
	
	@Override
	public void onLocationChanged(Location newLocation) {
		// 1) If there is no mLastLocationReading, store newLocation.
		// 2) If the newLocation iw newer than mLastLocationReading, store new location.
		if (mLastLocationReading == null) {
			mLastLocationReading = newLocation;
		} else if (newLocation.getTime() > mLastLocationReading.getTime()) {
			mLastLocationReading = newLocation;
		}
	}
	
	

	@Override
	public void onProviderDisabled(String provider) { /** not implemented */ }
	
	@Override
	public void onProviderEnabled(String provider) { /** not implemented */ }
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { /** not implemented */ }

	
	
	
	private long age(Location location) {
		//get the time now, minutes the last time location was obtained.
		return System.currentTimeMillis() - location.getTime();
	}

	
	
	
	private Location bestLastKnownLocation(float minAccuracy, long minTime) {
		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		List<String> matchingProviders = mLocationManager.getAllProviders();

		for (String provider : matchingProviders) {
			Location location = mLocationManager.getLastKnownLocation(provider);

			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if (accuracy < bestAccuracy) {
					bestResult = location;
					bestAccuracy = accuracy;
					bestTime = time;
				}
			}
		}

		// Return null location if:
		// (1) New location is less accurate than last location
		// (2) New location was retrieved less than 5 minutes ago.
		if (bestAccuracy > minAccuracy || bestTime < minTime) {
			return null;
		} else {
			return bestResult;
		}
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

	
	
	private void toggleFooter(boolean disableFooter) {
		if (disableFooter) {
			mFooterView.setEnabled(false);
			mFooterView.setAlpha(0.2f);	
		} else {
			mFooterView.setEnabled(true);
			mFooterView.setAlpha(1);	
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
