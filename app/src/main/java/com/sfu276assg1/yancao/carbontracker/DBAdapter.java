package com.sfu276assg1.yancao.carbontracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Data base to keep all the entry from the singleton
 */
public class DBAdapter {

    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 27;


    public static final String JOURNEY_DATE = "date";
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
    public static final String CAR_TRANS = "transmission";

    public static final String ROUTE_TYPE = "type";
    public static final String ROUTE_NAME = "rName";
    public static final String ROUTE_DISTANCE = "distance";
    public static final String ROUTE_LOWE = "lowEDis";
    public static final String ROUTE_HIGHE = "highEDis";

    public static final String BILL_STARTDATE = "startDate";
    public static final String BILL_ENDDATE = "endDate";
    public static final String BILL_ELECTRICITY = "electricity";
    public static final String BILL_GAS = "gas";
    public static final String BILL_PEOPLENUM = "peopleNum";
    public static final String BILL_RECORDDATE = "recordDate";

    public static final String DATABASE_NAME = "MyDb";
    private static final String TABLE_CAR = "car";
    private static final String TABLE_ROUTE = "route";
    private static final String TABLE_BUSROUTE = "busroute";
    private static final String TABLE_WALKROUTE = "walkroute";
    private static final String TABLE_JOURNEY = "journey";
    private static final String TABLE_BILL = "bill";

    //create tables
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
            CAR_TRANS + " STRING," +
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
            JOURNEY_DATE + " STRING," +
            CAR_NICKNAME + " STRING," +
            CAR_MAKE + " STRING," +
            CAR_MODEL + " STRING,"+
            CAR_YEAR + " STRING," +
            CAR_HIGHWAYE + " INT," +
            CAR_CITYE + " INT," +
            CAR_CYLINDER + " INT," +
            CAR_DISPLACEMENT + " DOUBLE," +
            CAR_FUELTYPE + " STRING," +
            CAR_TRANS + " STRING," +
            CAR_DRIVE + " DOUBLE,"+
            ROUTE_TYPE + " STRING," +
            ROUTE_NAME + " STRING," +
            ROUTE_DISTANCE + " DOUBLE," +
            ROUTE_LOWE + " DOUBLE," +
            ROUTE_HIGHE + " DOUBLE" +")";

    private static final String CREATE_TABLE_BILL = "CREATE TABLE "
            + TABLE_BILL + "(" + KEY_ROWID + " INTEGER PRIMARY KEY," +
            BILL_STARTDATE + " STRING," +
            BILL_ENDDATE + " STRING," +
            BILL_RECORDDATE + " STRING," +
            BILL_ELECTRICITY + " DOUBLE," +
            BILL_GAS + " DOUBLE," +
            BILL_PEOPLENUM + " INT" +")";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }

    //insert rows
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
        initialValues.put(CAR_TRANS, car.getTransmission());
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
        initialValues.put(JOURNEY_DATE,journey.getDate());
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
        initialValues.put(CAR_TRANS, car.getTransmission());
        initialValues.put(ROUTE_TYPE, route.getType());
        initialValues.put(ROUTE_NAME, route.getName());
        initialValues.put(ROUTE_DISTANCE, route.getDistance());
        initialValues.put(ROUTE_LOWE, route.getLowEDis());
        initialValues.put(ROUTE_HIGHE, route.getHighEDis());
        return db.insert(TABLE_JOURNEY, null, initialValues);
    }

    public long insertBillRow(Bill bill) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(BILL_STARTDATE, bill.getStartDate());
        initialValues.put(BILL_ENDDATE, bill.getEndDate());
        initialValues.put(BILL_RECORDDATE, bill.getRecordDate());
        initialValues.put(BILL_ELECTRICITY, bill.getElectricity());
        initialValues.put(BILL_GAS, bill.getGas());
        initialValues.put(BILL_PEOPLENUM, bill.getPeople());
        return db.insert(TABLE_BILL, null, initialValues);
    }

    //delete rows
    public void deleteCarRow(int index) {
        String where = KEY_ROWID + "='" + index +"'";
        db.delete(TABLE_CAR, where,null);
    }

    public void deleteRouteRow(int index) {
        String where = KEY_ROWID + "='" + index +"'";
        db.delete(TABLE_ROUTE, where, null);
    }

    public void deleteBusRouteRow(int index) {
        String where = KEY_ROWID + "='" + index +"'";
        db.delete(TABLE_BUSROUTE, where, null);
    }

    public void deleteWalkRouteRow(int index) {
        String where = KEY_ROWID + "='" + index +"'";
        db.delete(TABLE_WALKROUTE, where, null);
    }

    public void deleteJourneyRow (int index) {
        String where = KEY_ROWID + "='" + index +"'";
        db.delete(TABLE_JOURNEY,where,null);
    }

    public void deleteBillRow (int index) {
        String where = KEY_ROWID + "='" + index +"'";
        db.delete(TABLE_BILL,where,null);
    }

    //update rows (for editing car/route)
    public void updateCarRow(Car oldCar, Car update){
        String where = CAR_NICKNAME + "='" + oldCar.getNickname() +
                "'and "+ CAR_MAKE + "='" + oldCar.getMake() +
                "'and "+ CAR_MODEL + "='" + oldCar.getModel() +
                "'and "+ CAR_YEAR + "='" + oldCar.getYear()+ "'";
        ContentValues updateCar = new ContentValues();
        updateCar.put(CAR_NICKNAME, update.getNickname());
        updateCar.put(CAR_MAKE, update.getMake());
        updateCar.put(CAR_MODEL, update.getModel());
        updateCar.put(CAR_YEAR, update.getYear());
        updateCar.put(CAR_HIGHWAYE, update.getHighwayE());
        updateCar.put(CAR_CITYE, update.getCityE());
        updateCar.put(CAR_CYLINDER, update.getCylinders());
        updateCar.put(CAR_DISPLACEMENT, update.getDisplacement());
        updateCar.put(CAR_DRIVE, update.getDrive());
        updateCar.put(CAR_FUELTYPE, update.getFuelType());
        updateCar.put(CAR_DRIVE, update.getTransmission());
        db.update(TABLE_CAR, updateCar, where, null);
    }

    public void updateRouteRow(Route route, Route update){
        String where = ROUTE_NAME + "='" + route.getName() +
                "'and "+ ROUTE_TYPE + "='" + route.getType()+
                "'and "+ ROUTE_DISTANCE + "='" + route.getDistance()+
                "'and "+ ROUTE_LOWE + "='" + route.getLowEDis()+
                "'and "+ ROUTE_HIGHE + "='" + route.getHighEDis() +"'";

        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, update.getType());
        updateRoute.put(ROUTE_NAME, update.getName());
        updateRoute.put(ROUTE_DISTANCE,update.getDistance());
        updateRoute.put(ROUTE_LOWE, update.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, update.getHighEDis());
        db.update(TABLE_ROUTE, updateRoute, where, null);
    }

    public void updateBusRouteRow(Route route, Route update){
        String where = ROUTE_NAME + "='" + route.getName() +
                "'and "+ ROUTE_TYPE + "='" + route.getType()+
                "'and "+ ROUTE_DISTANCE + "='" + route.getDistance()+
                "'and "+ ROUTE_LOWE + "='" + route.getLowEDis()+
                "'and "+ ROUTE_HIGHE + "='" + route.getHighEDis() +"'";
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, update.getType());
        updateRoute.put(ROUTE_NAME, update.getName());
        updateRoute.put(ROUTE_DISTANCE,update.getDistance());
        updateRoute.put(ROUTE_LOWE, update.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, update.getHighEDis());
        db.update(TABLE_BUSROUTE, updateRoute, where, null);
    }

    public void updateWalkRouteRow(Route route, Route update){
        String where = ROUTE_NAME + "='" + route.getName() +
                "'and "+ ROUTE_TYPE + "='" + route.getType()+
                "'and "+ ROUTE_DISTANCE + "='" + route.getDistance()+
                "'and "+ ROUTE_LOWE + "='" + route.getLowEDis()+
                "'and "+ ROUTE_HIGHE + "='" + route.getHighEDis() +"'";
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, update.getType());
        updateRoute.put(ROUTE_NAME, update.getName());
        updateRoute.put(ROUTE_DISTANCE,update.getDistance());
        updateRoute.put(ROUTE_LOWE, update.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, update.getHighEDis());
        db.update(TABLE_WALKROUTE, updateRoute, where, null);
    }

    public void updateCarInJourney(Car oldCar, Car update){
        String where = CAR_NICKNAME + "='" + oldCar.getNickname() +
                "'and "+ CAR_MAKE + "='" + oldCar.getMake() +
                "'and "+ CAR_MODEL + "='" + oldCar.getModel() +
                "'and "+ CAR_YEAR + "='" + oldCar.getYear()+ "'";
        ContentValues updateCar = new ContentValues();
        updateCar.put(CAR_NICKNAME, update.getNickname());
        updateCar.put(CAR_MAKE, update.getMake());
        updateCar.put(CAR_MODEL, update.getModel());
        updateCar.put(CAR_YEAR, update.getYear());
        updateCar.put(CAR_HIGHWAYE, update.getHighwayE());
        updateCar.put(CAR_CITYE, update.getCityE());
        updateCar.put(CAR_CYLINDER, update.getCylinders());
        updateCar.put(CAR_DISPLACEMENT, update.getDisplacement());
        updateCar.put(CAR_DRIVE, update.getDrive());
        updateCar.put(CAR_FUELTYPE, update.getFuelType());
        updateCar.put(CAR_DRIVE, update.getTransmission());
        db.update(TABLE_JOURNEY,updateCar,where,null);
    }

    public void updateRouteInJourney(Route oldRoute,Route update){
        String where = ROUTE_NAME + "='" + oldRoute.getName() +
                "'and "+ ROUTE_TYPE + "='" + oldRoute.getType()+
                "'and "+ ROUTE_DISTANCE + "='" + oldRoute.getDistance()+
                "'and "+ ROUTE_LOWE + "='" + oldRoute.getLowEDis()+
                "'and "+ ROUTE_HIGHE + "='" + oldRoute.getHighEDis() +"'";
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, update.getType());
        updateRoute.put(ROUTE_NAME, update.getName());
        updateRoute.put(ROUTE_DISTANCE,update.getDistance());
        updateRoute.put(ROUTE_LOWE, update.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, update.getHighEDis());
        db.update(TABLE_JOURNEY, updateRoute, where, null);
    }

    public void updateDateInJourney(int index, String s){
        String where = KEY_ROWID + "='" + index +"'";
        ContentValues updateDate = new ContentValues();
        updateDate.put(JOURNEY_DATE,s);
        db.update(TABLE_JOURNEY,updateDate,where,null);
    }

    public void updateSingleCarInJourney(int index, Car update){
        String where = KEY_ROWID + "='" + index +"'";
        ContentValues updateCar = new ContentValues();
        updateCar.put(CAR_NICKNAME, update.getNickname());
        updateCar.put(CAR_MAKE, update.getMake());
        updateCar.put(CAR_MODEL, update.getModel());
        updateCar.put(CAR_YEAR, update.getYear());
        updateCar.put(CAR_HIGHWAYE, update.getHighwayE());
        updateCar.put(CAR_CITYE, update.getCityE());
        updateCar.put(CAR_CYLINDER, update.getCylinders());
        updateCar.put(CAR_DISPLACEMENT, update.getDisplacement());
        updateCar.put(CAR_DRIVE, update.getDrive());
        updateCar.put(CAR_FUELTYPE, update.getFuelType());
        updateCar.put(CAR_DRIVE, update.getTransmission());
        db.update(TABLE_JOURNEY,updateCar,where,null);
    }

    public void updateSingleRouteInJourney(int index, Route update){
        String where = KEY_ROWID + "='" + index +"'";
        ContentValues updateRoute = new ContentValues();
        updateRoute.put(ROUTE_TYPE, update.getType());
        updateRoute.put(ROUTE_NAME, update.getName());
        updateRoute.put(ROUTE_DISTANCE,update.getDistance());
        updateRoute.put(ROUTE_LOWE, update.getLowEDis());
        updateRoute.put(ROUTE_HIGHE, update.getHighEDis());
        db.update(TABLE_JOURNEY, updateRoute, where, null);
    }

    public void updateBillRow(int index, Bill update){
        String where = KEY_ROWID + "='" + index +"'";
        ContentValues updateBill = new ContentValues();
        updateBill.put(BILL_STARTDATE, update.getStartDate());
        updateBill.put(BILL_ENDDATE, update.getEndDate());
        updateBill.put(BILL_RECORDDATE, update.getRecordDate());
        updateBill.put(BILL_ELECTRICITY,update.getElectricity());
        updateBill.put(BILL_GAS, update.getGas());
        updateBill.put(BILL_PEOPLENUM, update.getPeople());
        db.update(TABLE_BILL, updateBill, where, null);
    }

    //get Lists for populate listview
    public CarCollection getCarList() {
        CarCollection carCollection = new CarCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_CAR;
        Cursor c = db.rawQuery(selectQuery, null);
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
                car.setTransmission(c.getString(c.getColumnIndex(CAR_TRANS)));

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
        if (c.moveToFirst()) {
            do {
                String date = c.getString(c.getColumnIndex(JOURNEY_DATE));
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
                car.setTransmission(c.getString(c.getColumnIndex(CAR_TRANS)));
                Route route = new Route();
                route.setName(c.getString(c.getColumnIndex(ROUTE_NAME)));
                route.setType(c.getString(c.getColumnIndex(ROUTE_TYPE)));
                route.setLowEDis(c.getDouble(c.getColumnIndex(ROUTE_LOWE)));
                route.setHighEDis(c.getDouble(c.getColumnIndex(ROUTE_HIGHE)));
                route.setDistance(c.getDouble(c.getColumnIndex(ROUTE_DISTANCE)));

                Journey journey = new Journey(car,route);
                journey.setDate(date);
                journeyCollection.addJourney(journey);

            } while (c.moveToNext());
        }
        c.close();
        return journeyCollection;
    }

    public BillCollection getBillList() {
        BillCollection billCollection = new BillCollection();
        String selectQuery = "SELECT  * FROM " + TABLE_BILL;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setStartDate(c.getString(c.getColumnIndex(BILL_STARTDATE)));
                bill.setEndDate(c.getString(c.getColumnIndex(BILL_ENDDATE)));
                bill.setRecordDate(c.getString(c.getColumnIndex(BILL_RECORDDATE)));
                bill.setElectricity(c.getDouble(c.getColumnIndex(BILL_ELECTRICITY)));
                bill.setGas(c.getDouble(c.getColumnIndex(BILL_GAS)));
                bill.setPeople(c.getInt(c.getColumnIndex(BILL_PEOPLENUM)));
                billCollection.addBill(bill);
            } while (c.moveToNext());
        }
        c.close();
        return billCollection;
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
            _db.execSQL(CREATE_TABLE_BILL);
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
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
