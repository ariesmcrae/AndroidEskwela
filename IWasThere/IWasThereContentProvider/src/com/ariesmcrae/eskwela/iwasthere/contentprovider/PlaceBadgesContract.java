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
package com.ariesmcrae.eskwela.iwasthere.contentprovider;

import android.net.Uri;

/** @author aries@ariesmcrae.com */
public final class PlaceBadgesContract {

	public static final String AUTHORITY = "com.ariesmcrae.eskwela.iwasthere.contentprovider";
	public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY + "/");

	public static final String BADGES_TABLE_NAME = "badges";

	// The URI for this table.
	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, BADGES_TABLE_NAME);

	public static final String _ID = "_id";
	public static final String FLAG_BITMAP_PATH = "flagBitmapPath";
	public static final String COUNTRY_NAME = "countryName";
	public static final String PLACE_NAME = "placeName";
	public static final String LAT = "lat";
	public static final String LON = "lon";

}
