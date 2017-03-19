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
    public static final String CAR_NICKNAME = "cName";
    public static final String CAR_MAKE = "make";
    public static final String CAR_MODEL = "model";
    public static final String CAR_YEAR = "year";
    public static final String CAR_HIGHWAYE = "highway";
    public static final String CAR_CITYE = "city";
    public static final String CAR_CYLINDER = "cylinder";
    public static final String CAR_DISPLACEMENT = "displacement";
    public static final String CAR_FUELTYPE = "fueltype";
    public static final String CAR_DRIVE = "drive";

    public static final String ROUTE_TYPE = "type";
    public static final String ROUTE_NAME = "rName";
    public static final String ROUTE_DISTANCE = "distance";
    public static final String ROUTE_LOWE = "lowEDis";
    public static final String ROUTE_HIGHE = "highEDis";


    //public static final String[] ALL_KEYS = new String[] {KEY_ROWID, ROUTE_TYPE, ROUTE_NAME, ROUTE_DISTANCE, ROUTE_LOWE, ROUTE_HIGHE};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb";
    private static final String TABLE_CAR = "car";
    private static final String TABLE_ROUTE = "route";
    private static final String TABLE_BUSROUTE = "busroute";
    private static final String TABLE_WALKROUTE = "walkroute";
    private static final String TABLE_JOURNEY = "journey";
    //private static final String TABLE_UTILITY = "utility";

    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 6;

    private static final String CREATE_TABLE_CAR = "CREATE TABLE "
            + TABLE_CAR + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            CAR_NICKNAME + " STRING," +
            CAR_MAKE + " STRING," +
            CAR_MODEL + " STRING,"+
            CAR_YEAR + " STRING," +
            CAR_HIGHWAYE + " INT," +
            CAR_CITYE + " INT," +
            CAR_CYLINDER + " INT," +
            CAR_DISPLACEMENT + " DOUBLE," +
            CAR_FUELTYPE + " STRING," +
            CAR_DRIVE + " DOUBLE "+")";

    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE "
            + TABLE_ROUTE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            ROUTE_TYPE + " STRING," +
            ROUTE_NAME + " STRING," +
            ROUTE_DISTANCE + " DOUBLE," +
            ROUTE_LOWE + " DOUBLE," +
            ROUTE_HIGHE + " DOUBLE" +")";

    private static final String CREATE_TABLE_BUSROUTE = "CREATE TABLE "
            + TABLE_BUSROUTE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            ROUTE_TYPE + " STRING," +
            ROUTE_NAME + " STRING," +
            ROUTE_DISTANCE + " DOUBLE," +
            ROUTE_LOWE + " DOUBLE," +
            ROUTE_HIGHE + " DOUBLE" +")";

    private static final String CREATE_TABLE_WALKROUTE = "CREATE TABLE "
            + TABLE_WALKROUTE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            ROUTE_TYPE + " STRING," +
            ROUTE_NAME + " STRING," +
            ROUTE_DISTANCE + " DOUBLE," +
            ROUTE_LOWE + " DOUBLE," +
            ROUTE_HIGHE + " DOUBLE" +")";

    private static final String CREATE_TABLE_JOURNEY = "CREATE TABLE "
            + TABLE_JOURNEY + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            CAR_NICKNAME + " STRING," +
            CAR_MAKE + " STRING," +
            CAR_MODEL + " STRING,"+
            CAR_YEAR + " STRING," +
            CAR_HIGHWAYE + " INT," +
            CAR_CITYE + " INT," +
            CAR_CYLINDER + " INT," +
            CAR_DISPLACEMENT + " DOUBLE," +
            CAR_FUELTYPE + " STRING," +
            CAR_DRIVE + " DOUBLE,"+
            ROUTE_TYPE + " STRING," +
            ROUTE_NAME + " STRING," +
            ROUTE_DISTANCE + " DOUBLE," +
            ROUTE_LOWE + " DOUBLE," +
            ROUTE_HIGHE + " DOUBLE" +")";

    //UTILITY TABLE

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
    public long insertCarRow(Car car) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CAR_NICKNAME, car.getNickname());
        initialValues.put(CAR_MAKE, car.getMake());
        initialValues.put(CAR_MODEL, car.getModel());
        initialValues.put(CAR_YEAR, car.getYear());
        initialValues.put(CAR_HIGHWAYE, car.getHighwayE());
        initialValues.put(CAR_CITYE, car.getCityE());
        initialValues.put(CAR_CYLINDER, car.getCylinders());
        initialValues.put(CAR_DISPLACEMENT, car.getDisplacement());
        initialValues.put(CAR_DRIVE, car.getDrive());
        initialValues.put(CAR_FUELTYPE, car.getFuelType());
        initialValues.put(CAR_DRIVE, car.getTransmission());

        return db.insert(TABLE_CAR, null, initialValues);
    }

    public long insertRouteRow(Route route) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROUTE_TYPE, route.getType());
        initialValues.put(ROUTE_NAME, route.getName());
        initialValues.put(ROUTE_DISTANCE, route.getDistance());
        initialValues.put(ROUTE_LOWE, route.getLowEDis());
        initialValues.put(ROUTE_HIGHE, route.getHighEDis());

        return db.insert(TABLE_ROUTE, null, initialValues);
    }

    public long insertBusRouteRow(Route route) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROUTE_TYPE, route.getType());
        initialValues.put(ROUTE_NAME, route.getName());
        initialValues.put(ROUTE_DISTANCE, route.getDistance());
        initialValues.put(ROUTE_LOWE, route.getLowEDis());
        initialValues.put(ROUTE_HIGHE, route.getHighEDis());

        return db.insert(TABLE_BUSROUTE, null, initialValues);
    }

    public long insertWalkRouteRow(Route route) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ROUTE_TYPE, route.getType());
        initialValues.put(ROUTE_NAME, route.getName());
        initialValues.put(ROUTE_DISTANCE, route.getDistance());
        initialValues.put(ROUTE_LOWE, route.getLowEDis());
        initialValues.put(ROUTE_HIGHE, route.getHighEDis());

        return db.insert(TABLE_WALKROUTE, null, initialValues);
    }

    public long insertRowJourney(Journey journey) {
        ContentValues initialValues = new ContentValues();
        Car car = journey.getCar();
        Route route = journey.getRoute();
        initialValues.put(CAR_NICKNAME, car.getNickname());
        initialValues.put(CAR_MAKE, car.getMake());
        initialValues.put(CAR_MODEL, car.getModel());
        initialValues.put(CAR_YEAR, car.getYear());
        initialValues.put(CAR_HIGHWAYE, car.getHighwayE());
        initialValues.put(CAR_CITYE, car.getCityE());
        initialValues.put(CAR_CYLINDER, car.getCylinders());
        initialValues.put(CAR_DISPLACEMENT, car.getDisplacement());
        initialValues.put(CAR_DRIVE, car.getDrive());
        initialValues.put(CAR_FUELTYPE, car.getFuelType());
        initialValues.put(CAR_DRIVE, car.getTransmission());

        initialValues.put(ROUTE_TYPE, route.getType());
        initialValues.put(ROUTE_NAME, route.getName());
        initialValues.put(ROUTE_DISTANCE, route.getDistance());
        initialValues.put(ROUTE_LOWE, route.getLowEDis());
        initialValues.put(ROUTE_HIGHE, route.getHighEDis());

        return db.insert(TABLE_JOURNEY, null, initialValues);
    }


    public void deleteCarRow(String s) {
        db.delete(TABLE_CAR, CAR_NICKNAME + "= ?", new String[] { s });
    }

    public void deleteRouteRow(String s) {
        db.delete(TABLE_ROUTE, ROUTE_NAME + "= ?", new String[] { s });
    }

    public void deleteBusRouteRow(String s) {
        db.delete(TABLE_BUSROUTE, ROUTE_NAME + "= ?", new String[] { s });
    }

    public void deleteWalkRouteRow(String s) {
        db.delete(TABLE_WALKROUTE, ROUTE_NAME + "= ?", new String[] { s });
    }

    //DELETE JOURNEY


    public void updateCarRow(String s, Car car){
        String where = CAR_NICKNAME + "=" + s;
        ContentValues updateCar = new ContentValues();
        updateCar.put(CAR_NICKNAME, car.getNickname());
        updateCar.put(CAR_MAKE, car.getMake());
        updateCar.put(CAR_MODEL, car.getModel());
        updateCar.put(CAR_YEAR, car.getYear());
        updateCar.put(CAR_HIGHWAYE, car.getHighwayE());
        updateCar.put(CAR_CITYE, car.getCityE());
        updateCar.put(CAR_CYLINDER, car.getCylinders());
        updateCar.put(CAR_DISPLACEMENT, car.getDisplacement());
        updateCar.put(CAR_DRIVE, car.getDrive());
        updateCar.put(CAR_FUELTYPE, car.getFuelType());
        updateCar.put(CAR_DRIVE, car.getTransmission());

        // Insert it into the database.
        db.update(TABLE_CAR, updateCar, where, null);
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

    public void updateBusRouteRow(String s, Route route){
        String where = ROUTE_NAME + "=" + s;
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, route.getType());
        updateRoute.put(ROUTE_NAME, route.getName());
        updateRoute.put(ROUTE_DISTANCE,route.getDistance());
        updateRoute.put(ROUTE_LOWE, route.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, route.getHighEDis());

        // Insert it into the database.
        db.update(TABLE_BUSROUTE, updateRoute, where, null);
    }

    public void updateWalkRouteRow(String s, Route route){
        String where = ROUTE_NAME + "=" + s;
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, route.getType());
        updateRoute.put(ROUTE_NAME, route.getName());
        updateRoute.put(ROUTE_DISTANCE,route.getDistance());
        updateRoute.put(ROUTE_LOWE, route.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, route.getHighEDis());

        // Insert it into the database.
        db.update(TABLE_WALKROUTE, updateRoute, where, null);
    }

    public CarCollection getCarList() {
        CarCollection carCollection = new CarCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_CAR;
        Cursor c = db.rawQuery(selectQuery, null);
        // return all cars in ListView
        if (c.moveToFirst()) {
            do {
                Car car = new Car();
                car.setNickname(c.getString(c.getColumnIndex(CAR_NICKNAME)));
                car.setMake(c.getString(c.getColumnIndex(CAR_MAKE)));
                car.setModel(c.getString(c.getColumnIndex(CAR_MODEL)));
                car.setYear(c.getString(c.getColumnIndex(CAR_YEAR)));
                car.setHighwayE(c.getInt(c.getColumnIndex(CAR_HIGHWAYE)));
                car.setCityE(c.getInt(c.getColumnIndex(CAR_CITYE)));
                car.setCylinders(c.getInt(c.getColumnIndex(CAR_CYLINDER)));
                car.setDisplacement(c.getDouble(c.getColumnIndex(CAR_DISPLACEMENT)));
                car.setFuelType(c.getString(c.getColumnIndex(CAR_FUELTYPE)));
                car.setDrive(c.getString(c.getColumnIndex(CAR_DRIVE)));

                carCollection.addCar(car);
            } while (c.moveToNext());
        }
        c.close();
        return carCollection;
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

    public RouteCollection getBusRouteList() {
        RouteCollection routeCollection = new RouteCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_BUSROUTE;
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

    public RouteCollection getWalkRouteList() {
        RouteCollection routeCollection = new RouteCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_WALKROUTE;
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

    public JourneyCollection getJourneyList() {
        JourneyCollection journeyCollection = new JourneyCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNEY;
        Cursor c = db.rawQuery(selectQuery, null);
        // return all cars in ListView
        if (c.moveToFirst()) {
            do {
                Car car = new Car();
                car.setNickname(c.getString(c.getColumnIndex(CAR_NICKNAME)));
                car.setMake(c.getString(c.getColumnIndex(CAR_MAKE)));
                car.setModel(c.getString(c.getColumnIndex(CAR_MODEL)));
                car.setYear(c.getString(c.getColumnIndex(CAR_YEAR)));
                car.setHighwayE(c.getInt(c.getColumnIndex(CAR_HIGHWAYE)));
                car.setCityE(c.getInt(c.getColumnIndex(CAR_CITYE)));
                car.setCylinders(c.getInt(c.getColumnIndex(CAR_CYLINDER)));
                car.setDisplacement(c.getDouble(c.getColumnIndex(CAR_DISPLACEMENT)));
                car.setFuelType(c.getString(c.getColumnIndex(CAR_FUELTYPE)));
                car.setDrive(c.getString(c.getColumnIndex(CAR_DRIVE)));
                Route route = new Route();
                route.setName(c.getString(c.getColumnIndex(ROUTE_NAME)));
                route.setType(c.getString(c.getColumnIndex(ROUTE_TYPE)));
                route.setLowEDis(c.getDouble(c.getColumnIndex(ROUTE_LOWE)));
                route.setHighEDis(c.getDouble(c.getColumnIndex(ROUTE_HIGHE)));
                route.setDistance(c.getDouble(c.getColumnIndex(ROUTE_DISTANCE)));
                journeyCollection.addJourney(new Journey(car,route));

            } while (c.moveToNext());
        }
        c.close();
        return journeyCollection;
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
            _db.execSQL(CREATE_TABLE_CAR);
            _db.execSQL(CREATE_TABLE_ROUTE);
            _db.execSQL(CREATE_TABLE_BUSROUTE);
            _db.execSQL(CREATE_TABLE_WALKROUTE);
            _db.execSQL(CREATE_TABLE_JOURNEY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAR);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSROUTE);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALKROUTE);
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEY);

            // Recreate new database:
            onCreate(_db);
        }
    }
}