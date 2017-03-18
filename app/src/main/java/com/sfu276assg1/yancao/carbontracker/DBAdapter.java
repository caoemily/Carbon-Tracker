package com.sfu276assg1.yancao.carbontracker;

/**
 * Created by yancao on 2017-03-17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.
public class DBAdapter {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String ROUTE_TYPE = "type";
    public static final String ROUTE_NAME = "rName";
    public static final String ROUTE_DISTANCE = "distance";
    public static final String ROUTE_LOWE = "lowEDis";
    public static final String ROUTE_HIGHE = "highEDis";


    //public static final String[] ALL_KEYS = new String[] {KEY_ROWID, ROUTE_TYPE, ROUTE_NAME, ROUTE_DISTANCE, ROUTE_LOWE, ROUTE_HIGHE};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    public static final String TABLE_ROUTE = "routeTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 5;

    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE "
            + TABLE_ROUTE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            ROUTE_TYPE + " STRING," +
            ROUTE_NAME + " STRING," +
            ROUTE_DISTANCE + " DOUBLE," +
            ROUTE_LOWE + " DOUBLE," +
            ROUTE_HIGHE + " DOUBLE" +")";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRouteRow(Route route) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROUTE_TYPE, route.getType());
        initialValues.put(ROUTE_NAME, route.getName());
        initialValues.put(ROUTE_DISTANCE, route.getDistance());
        initialValues.put(ROUTE_LOWE, route.getLowEDis());
        initialValues.put(ROUTE_HIGHE, route.getHighEDis());

        return db.insert(TABLE_ROUTE, null, initialValues);


    }

    public void deleteRouteRow(String s) {
        db.delete(TABLE_ROUTE, ROUTE_NAME + "= ?", new String[] { s });
    }
    

    public void updateRouteRow(String s, Route route){
        String where = ROUTE_NAME + "=" + s;
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, route.getType());
        updateRoute.put(ROUTE_NAME, route.getName());
        updateRoute.put(ROUTE_DISTANCE,route.getDistance());
        updateRoute.put(ROUTE_LOWE, route.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, route.getHighEDis());

        // Insert it into the database.
        db.update(TABLE_ROUTE, updateRoute, where, null);
    }


    public RouteCollection getRouteList() {
        RouteCollection routeCollection = new RouteCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_ROUTE;
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Route route = new Route();
                route.setName(c.getString(c.getColumnIndex(ROUTE_NAME)));
                route.setType(c.getString(c.getColumnIndex(ROUTE_TYPE)));
                route.setLowEDis(c.getDouble(c.getColumnIndex(ROUTE_LOWE)));
                route.setHighEDis(c.getDouble(c.getColumnIndex(ROUTE_HIGHE)));
                route.setDistance(c.getDouble(c.getColumnIndex(ROUTE_DISTANCE)));

                routeCollection.addRoute(route);
            } while (c.moveToNext());
        }
        c.close();
        return routeCollection;
    }





    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(CREATE_TABLE_ROUTE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
