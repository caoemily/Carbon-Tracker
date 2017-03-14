package com.sfu276assg1.yancao.carbontracker;

/**
 * Singleton for seprating UI and java classes
 */
import java.util.ArrayList;

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();

    private JourneyCollection journeyCollection;

    private RouteCollection routeCollection;
    private CarCollection carCollection;

    private CarFamily carFromFile;

    public static CarbonModel getInstance() {
        return ourInstance;
    }

    private CarbonModel() {
        journeyCollection = new JourneyCollection();
        routeCollection = new RouteCollection();
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
    public void addRoute(Route route) {
        routeCollection.addRoute(route);
    }
    public Route getRoute(int index) {
        return routeCollection.getRoute(index);
    }
    public void changeRoute(Route route, int indexOfChanging) {
        routeCollection.changeRoute(route, indexOfChanging);
    }
    public void removeRoute(int index) {
        routeCollection.remove(index);
    }
}
