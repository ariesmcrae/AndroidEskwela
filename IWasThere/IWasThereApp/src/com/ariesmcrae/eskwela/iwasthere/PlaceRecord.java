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

import android.graphics.Bitmap;
import android.location.Location;

/** @author aries@ariesmcrae.com */
public class PlaceRecord {

    // URL for retrieving the flag image
    private String mFlagUrl;

    // path to flag image in external memory
    private String mFlagBitmapPath;

    private String mCountryName;
    private String mPlaceName;
    private Bitmap mFlagBitmap;
    private double lat;
    private double lon;

    
    
    public PlaceRecord(String flagUrl, String flagBitmapPath, String countryName, String placeName, double lat, double lon) {
        mFlagUrl = flagUrl;
        mFlagBitmapPath = flagBitmapPath;
        mCountryName = countryName;
        mPlaceName = placeName;
        setLat(lat);
        setLon(lon);
    }

    
    
    public PlaceRecord(Location location) {
        setLat(location.getLatitude());
        setLon(location.getLongitude());
    }
    
    

    public void setLocation(Location location) {
        setLat(location.getLatitude());
        setLon(location.getLongitude());
    }
    
    

    public String getFlagUrl() { return mFlagUrl; }
    public void setFlagUrl(String flagUrl) { this.mFlagUrl = flagUrl; }

    public String getCountryName() { return mCountryName; }
    public void setCountryName(String country) { this.mCountryName = country; }

    public String getPlace() { return mPlaceName; }
    public void setPlace(String place) { this.mPlaceName = place; }

    public Bitmap getFlagBitmap() { return mFlagBitmap; }
    public void setFlagBitmap(Bitmap mFlagBitmap) { this.mFlagBitmap = mFlagBitmap; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public String getFlagBitmapPath() { return mFlagBitmapPath; }
    public void setFlagBitmapPath(String flagBitmapPath) { this.mFlagBitmapPath = flagBitmapPath; }
    
    
    
    public boolean intersects(Location location) {
        double tolerance = 1000;
        float[] results = new float[3];

        Location.distanceBetween(location.getLatitude(), location.getLongitude(), lat, lon, results);

        return (results[0] <= tolerance);
    }


    
    @Override
    public String toString() {
        return "Place: " + mPlaceName + " Country: " + mCountryName;
    }

}
