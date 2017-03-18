package com.sfu276assg1.yancao.carbontracker;

/**
 * Singleton for seprating UI and java classes
 */
import java.util.ArrayList;

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();

    private JourneyCollection journeyCollection;

    private RouteCollection routeCollection;
    private RouteCollection busRouteCollection;
    private RouteCollection walkRouteCollection;



    private CarCollection carCollection;

    private CarFamily carFromFile;

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
    }

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
    public void changeRoute(Route route, int indexOfChanging) {
        routeCollection.changeRoute(route, indexOfChanging);
    }
    public void changeBusRoute(Route route, int indexOfChanging) {
        busRouteCollection.changeRoute(route, indexOfChanging);
    }
    public void changeWalkRoute(Route route, int indexOfChanging) {
        walkRouteCollection.changeRoute(route, indexOfChanging);
    }
    public void removeRoute(int index) {
        routeCollection.remove(index);
    }
    public void removeBusRoute(int index) {
        busRouteCollection.remove(index);
    }
    public void removeWalkRoute(int index) {
        walkRouteCollection.remove(index);
    }
}
