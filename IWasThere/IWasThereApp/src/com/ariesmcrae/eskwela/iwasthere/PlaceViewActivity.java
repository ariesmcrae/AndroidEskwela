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
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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
import com.ariesmcrae.eskwela.iwasthere.R;
import com.ariesmcrae.eskwela.iwasthere.contentprovider.PlaceBadgesContract;

/** @author aries@ariesmcrae.com */
public class PlaceViewActivity extends ListActivity implements LocationListener, LoaderCallbacks<Cursor> {

    private static String TAG = "eskwela-IWasThere";

    private static final long FIVE_MINS = 5 * 60 * 1000;

    // The last valid location reading
    private Location mLastLocationReading;

    // The ListView's adapter
    private PlaceViewAdapter mCursorAdapter;

    // default minimum time between new location readings
    private long mMinTime = 5000;

    // default minimum distance between old and new readings.
    private float mMinDistance = 1000.0f;

    // Reference to the LocationManager
    private LocationManager mLocationManager;

    // A fake location provider used for testing
    private MockLocationProvider mMockLocationProvider;
    
    private TextView mFooterView = null;    

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = getListView();
        listView.setFooterDividersEnabled(true); // Puts a divider between places you've been to and FooterView
        
        mFooterView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null); // Loads up TextView from
        listView.addFooterView(mFooterView);        

        toggleFooter(true);
        
        // Create and set empty PlaceViewAdapter
        // Cursor cursor = getContentResolver().query(PlaceBadgesContract.CONTENT_URI, null, null, null, null);        
        mCursorAdapter = new PlaceViewAdapter(this, null, 0);
        listView.setAdapter(mCursorAdapter);        
        
        // Initialize a CursorLoader. After init is called, 
        // onCreateLoader is called - creates and returns a new loader for the specified id.
        getLoaderManager().initLoader(0, null, this);        
        
        
        // Acquire reference to the LocationManager
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if (mLocationManager == null) {
            finish();
        }

        // Get best last location measurement
        mLastLocationReading = bestLastKnownLocation(mMinDistance, FIVE_MINS);        
        
        mFooterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                log("Entered footerView.OnClickListener.onClick()");
                updateView();
            }
        });        
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
    
    
    
    
    private void updateView() {
        if (mLastLocationReading != null) {
            if (mCursorAdapter.intersects(mLastLocationReading)) {
                log("You already have this location badge");
                Toast.makeText(getApplicationContext(), "You already have this location badge", Toast.LENGTH_LONG).show();

            } else {
                log("Starting Place Download");
                new PlaceDownloaderTask(PlaceViewActivity.this).execute(mLastLocationReading);
            }
        } else {
            log("Location data is not available");
        }
    }    
    
    
    
    
    
    
    @Override
    protected void onResume() {
        super.onResume();
        mMockLocationProvider = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);

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
            }, FIVE_MINS, TimeUnit.MILLISECONDS); // Unregister location listeners after 5 mins
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

        mCursorAdapter.add(place);
    }

    
    
    @Override
    public void onLocationChanged(Location newLocation) {
        if (mLastLocationReading == null) {
            mLastLocationReading = newLocation;
        } else if (newLocation.getTime() > mLastLocationReading.getTime()) {
            mLastLocationReading = newLocation;
        }
    }

    
    
    // Instantiate and return a new Loader for the given ID. 
    // This is where the cursor is created.
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        log("Entered onCreateLoader()");

        String[] projection = null; // null returns all columns
        String selection = null; // null returns all rows. 
        String[] selectionArgs = null; // no parameterized query here such as '?' (like that in sql query), because 'selection' is null.
        String sortOrder = null; // null will use the default sort order.
        
        return new CursorLoader(this, PlaceBadgesContract.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    

    // Called when the previously created Loader has finished loading its data. 
    // This assigns the new Cursor but does not close the previous one.
    // This method receives the newly created loader and a cursor containing the relevant data.   
    // Loader monitors the source of its data and deliver new results when the content changes. 
    // In other words, CursorLoader auto updates and hence there is no need to requery the cursor.       
    @Override
    public void onLoadFinished(Loader<Cursor> newLoader, Cursor newCursor) {
        if (mCursorAdapter != null && newCursor != null) {
            mCursorAdapter.swapCursor(newCursor); //swap the new cursor in. 
        }
    }

    
    
    
    // Called when the last Cursor provided to onLoadFinished() is about to be reset, thus making its data unavailable. 
    // It is being reset in order to create a new cursor to query different data. 
    // This is called when the last Cursor provided to onLoadFinished() above is about to be closed. 
    // We need to make sure we are no longer using it.
    @Override
    public void onLoaderReset(Loader<Cursor> newLoader) {
        if (mCursorAdapter!= null) {
            mCursorAdapter.swapCursor(null);              
        }
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
            ArrayList<PlaceRecord> currData = mCursorAdapter.getList();
            for (int i = 0; i < currData.size(); i++) {
                log(currData.get(i).toString());
            }
            return true;
        case R.id.delete_badges:
            mCursorAdapter.removeAllViews();
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
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}
