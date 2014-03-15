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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/** @author aries@ariesmcrae.com */
public class PlaceBadgeContentProvider extends ContentProvider {

	private SQLiteDatabase database;
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Badges";
	

	private static final String CREATE_LOCATION_TABLE = " CREATE TABLE "
			+ PlaceBadgesContract.BADGES_TABLE_NAME + " (" + PlaceBadgesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PlaceBadgesContract.FLAG_BITMAP_PATH + " TEXT NOT NULL, "
			+ PlaceBadgesContract.COUNTRY_NAME + " TEXT NOT NULL, "
			+ PlaceBadgesContract.PLACE_NAME + " TEXT NOT NULL, "
			+ PlaceBadgesContract.LAT + " REAL NOT NULL, "
			+ PlaceBadgesContract.LON + " REAL NOT NULL);";

	
	

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		int rowsDeleted = database.delete(PlaceBadgesContract.BADGES_TABLE_NAME, null, null);
		getContext().getContentResolver().notifyChange(PlaceBadgesContract.CONTENT_URI, null);
		return rowsDeleted;
	}

	
	
	@Override
	public String getType(Uri arg0) {
		// unimplemented
		return null;
	}

	
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = database.insert(PlaceBadgesContract.BADGES_TABLE_NAME, "", values);

		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(
					PlaceBadgesContract.CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			
			return _uri;
		}
		throw new SQLException("Failed to add record into" + uri);
	}

	
	
	@Override
	public boolean onCreate() {
		DatabaseHelper dbHelper = new DatabaseHelper(getContext());
		database = dbHelper.getWritableDatabase();
		return (database != null);
	}

	
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(PlaceBadgesContract.BADGES_TABLE_NAME);

		Cursor c = qb.query(database, null, null, null, null, null, null);

		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	
	
	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// unimplemented
		return 0;
	}

	
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_LOCATION_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + PlaceBadgesContract.BADGES_TABLE_NAME);
			onCreate(db);
		}

	} //endClass	

	

}
