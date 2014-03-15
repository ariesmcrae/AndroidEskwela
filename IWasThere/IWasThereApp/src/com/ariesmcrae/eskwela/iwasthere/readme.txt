**************TL;DR version*****************

How does PlaceViewActivity loads SQLite data onto the screen?
==============================================================
PlaceViewActivity <== CursorLoader <== ContentResolver <== PlaceBadgeContentProvider.query(...) <== SQLite


How does PlaceViewActivity insert data into SQLite?
=======================================================
PlaceViewActivity ==> ContentResolver ==> PlaceBadgeContentProvider.insert(...) ==> SQLite


Why doesn't PlaceViewActivity talk to SQLite directly ?
=========================================================
So that the application's User Interface is not blocked.









*******************Long version********************

int flag = 0; 
mCursorAdapter = new PlaceViewAdapter(this, null, flag);

Zero means 'do not observe for data changes or auto-requery for data', because you're using CursorLoader. CursorLoader will observe the data changes/auto-requery of SQLite on the behalf of PlaceViewActivity.java


How does PlaceViewActivity get data from SQLite?
=================================================
PlaceViewActivity <== CursorLoader <== ContentResolver <== PlaceBadgeContentProvider <== SQLite

It gets data via CursorLoader. What the hell is a CursorLoader? Well...this is how it goes :
PlaceViewActivity talks to CursorLoader.
CursorLoader talks to PlaceBadgeContentProvider (via ContentResolver internally).
PlaceBadgeContentProvider.query(...) performs a query on SQLite.
PlaceBadgeContentProvider.query returns a cursor all the way back to PlaceViewActivity.onLoadFinished.
Once PlaceViewActivity has gotten a hold of that Cursor via PlaceViewActivity.onLoadFinished, you can then loop thru the Cursor to display data onto the screen (via swapCursor).

PlaceViewActivity.java never calls PlaceBadgeContentProvider.java directly, but via CursorLoader.

How does PlaceViewActivity use CursorLoader to talk to PlaceBadgeContentProvider? Do this inside PlaceViewActivity.onCreateLoader for CursorLoader to start talking to PlaceBadgeContentProvider:

String[] projection = null; // null returns all columns
String selection = null; // null returns all rows. 
String[] selectionArgs = null; // no parameterized query here such as '?' (like that in sql query), because 'selection' is null.
String sortOrder = null; // null will use the default sort order.
        
return new CursorLoader(this, PlaceBadgesContract.CONTENT_URI, projection, selection, selectionArgs, sortOrder);

How do you know you're using CursorLoader?  Because your PlaceViewActivity implements LoaderCallbacks.

PlaceViewActivity.onCreateLoader is where CursorLoader is initially created.

PlaceViewActivity.onCreate is where you initialize the CursorLoader.
getLoaderManager().initLoader(0, null, this); 

After .initLoader is called, PlaceViewActivity.onCreateLoader gets called.
After onCreateLoader gets called, onLoadFinished is called. 
onLoadFinished gives you newCursor which holds your data to display on the screen.


How does PlaceViewActivity insert data into SQLite?
=========================================================
Again, PlaceViewActivity never talks to SQLite directly. It does so via ContentResolver:
PlaceViewActivity ==> ContentResolver ==> PlaceBadgeContentProvider ==> SQLite

This is done in PlaceViewAdapter.add(...)
mContext.getContentResolver().insert(PlaceBadgesContract.CONTENT_URI, values);  

Why the heck doesn't PlaceViewActivity talk to SQLite directly when loading data?
======================================================================================
PlaceViewActivity uses CursorLoader (which in turn queries the ContentResolver) in the background thread so that the application's User Interface is not blocked.

