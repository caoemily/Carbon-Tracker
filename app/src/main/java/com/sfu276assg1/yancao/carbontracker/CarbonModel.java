package com.sfu276assg1.yancao.carbontracker;

import android.content.Context;

import java.util.ArrayList;

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();

    private JourneyCollection journeyCollection;
    private RouteCollection routeCollection;
    private RouteCollection busRouteCollection;
    private RouteCollection walkRouteCollection;
    private CarCollection carCollection;
    private CarFamily carFromFile;
    private BillCollection billCollection;
    private DBAdapter db;
    private Tips tips;
    private int unitChoice;
    private int journeyId;

    public static CarbonModel getInstance() {
        return ourInstance;
    }

    private CarbonModel() {
        journeyCollection = new JourneyCollection();
        routeCollection = new RouteCollection();
        busRouteCollection = new RouteCollection();
        walkRouteCollection = new RouteCollection();
        carCollection = new CarCollection();
        carFromFile = new CarFamily();
        billCollection = new BillCollection();
        tips = new Tips();
        unitChoice = 0;
        journeyId=0;
    }

    public int getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(int journeyId) {
        this.journeyId = journeyId;
    }

    public void setUnitChoice(int unit){this.unitChoice = unit;}
    public int getUnitChoice(){return this.unitChoice;}

    public void setDb(DBAdapter db){this.db = db;}
    public DBAdapter getDb(){return this.db;}

    public CarFamily getCarFromFile(){
        return carFromFile;
    }
    public void setCarFamily(ArrayList<Car> cars) {
        carFromFile.setCars(cars);
    }

    public JourneyCollection getJourneyCollection() {
        return journeyCollection;
    }
    public void setJourneyCollection(JourneyCollection collection) {this.journeyCollection = collection;}
    public void addJourney(Journey journey) {
        journeyCollection.addJourney(journey);
    }
    public Journey getLastJourney(){return journeyCollection.getLastJourney();}
    public void removeLastJourney() {
        journeyCollection.removeLastJourney();
    }
    public void changeCarInJourney(Car tempCar, Car car) {
        journeyCollection.changeCar(tempCar, car);
    }
    public void changeRouteInJourney(Route tempRoute, Route route) {
        journeyCollection.changeRoute(tempRoute, route);
    }
    public void removeJourney(int index) {journeyCollection.remove(index);}


    public CarCollection getCarCollection(){
        return carCollection;
    }
    public void setCarCollection(CarCollection collection) {this.carCollection = collection;}
    public void addCar(Car car) {
        carCollection.addCar(car);
    }
    public Car getCar(int index) {return carCollection.getCar(index);}
    public void removeCar(int index) {
        carCollection.remove(index);
    }
    public void changeCar(Car car, int indexOfChanging) {
        carCollection.changeCar(car, indexOfChanging);
    }

    public RouteCollection getRouteCollection() {
        return routeCollection;
    }
    public void setRouteCollection(RouteCollection collection){this.routeCollection = collection;}
    public RouteCollection getBusRouteCollection() {
        return busRouteCollection;
    }
    public void setBusRouteCollection(RouteCollection collection){this.busRouteCollection = collection;}
    public RouteCollection getWalkRouteCollection() {
        return walkRouteCollection;
    }
    public void setWalkRouteCollection(RouteCollection collection){this.walkRouteCollection = collection;}
    public void addRoute(Route route) {
        routeCollection.addRoute(route);
    }
    public void addBusRoute(Route route) {
        busRouteCollection.addRoute(route);
    }
    public void addWalkRoute(Route route) {
        walkRouteCollection.addRoute(route);
    }
    public Route getRoute(int index) {
        return routeCollection.getRoute(index);
    }
    public Route getBusRoute(int index){return busRouteCollection.getRoute(index);}
    public Route getWalkRoute(int index){return walkRouteCollection.getRoute(index);}
    public void changeRoute(Route route, int indexOfChanging) {routeCollection.changeRoute(route, indexOfChanging);}
    public void changeBusRoute(Route route, int indexOfChanging) {busRouteCollection.changeRoute(route, indexOfChanging);}
    public void changeWalkRoute(Route route, int indexOfChanging) {walkRouteCollection.changeRoute(route, indexOfChanging);}
    public void removeRoute(int index) {
        routeCollection.remove(index);
    }
    public void removeBusRoute(int index) {
        busRouteCollection.remove(index);
    }
    public void removeWalkRoute(int index) {
        walkRouteCollection.remove(index);
    }

    public BillCollection getBillCollection() {
        return billCollection;
    }
    public void setBillCollection(BillCollection collection){this.billCollection = collection;}
    public void addBill(Bill bill) {
        billCollection.addBill(bill);
    }
    public Bill getBill(int index) {
        return billCollection.getBill(index);
    }
    public void changeBill(Bill bill, int indexOfChanging) {
        billCollection.changeBill(bill, indexOfChanging);
    }
    public void removeBill(int index) {
        billCollection.remove(index);
    }

    public String generateCarTip(Context context, JourneyCollection journeyCollection, int index) {
        return tips.generateCarTip(context,journeyCollection,index);
    }
    public String generateElecTip(Context context, BillCollection billCollection, int index) {
        return tips.generateElectricityTip(context, billCollection,index);
    }
    public String generateGasTip(Context context, BillCollection billCollection, int index) {
        return tips.generateGasTip(context, billCollection,index);
    }
    public int showWhichTip(JourneyCollection journeyCollection,BillCollection billCollection) {
        return tips.showWhichTip(journeyCollection, billCollection);
    }
}
