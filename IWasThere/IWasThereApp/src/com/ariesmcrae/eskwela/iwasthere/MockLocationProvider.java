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


/** 
 * Adapted from code found at: http://mobiarch.wordpress.com/2012/07/17/testing-with-mock-location-data-in-android/
 * 
 * @author aries@ariesmcrae.com 
 */
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;

public class MockLocationProvider {

    private String mProviderName;
    private LocationManager mLocationManager;

    private static float mockAccuracy = 5;

    
    
    public MockLocationProvider(String name, Context ctx) {
        this.mProviderName = name;

        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.addTestProvider(mProviderName, false, false, false, false, true, true, true, 0, 5);
        mLocationManager.setTestProviderEnabled(mProviderName, true);
    }
    
    

    public void pushLocation(double lat, double lon) {
        Location mockLocation = new Location(mProviderName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mockLocation.setAccuracy(mockAccuracy);

        mLocationManager.setTestProviderLocation(mProviderName, mockLocation);
    }

    
    
    public void shutdown() {
        mLocationManager.removeTestProvider(mProviderName);
    }
}
