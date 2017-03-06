package com.sfu276assg1.yancao.carbontracker;

//Singleton for seprating UI and java classes

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();

    private JourneyCollection journeyCollection;

    private CarCollection carCollection;
    private RouteCollection routeCollection;

    private CarCollection allCar;
    private RouteCollection allRoute;

    public static CarbonModel getInstance() {
        return ourInstance;
    }

    private CarbonModel() {
        journeyCollection = new JourneyCollection();
        routeCollection = new RouteCollection();
        allRoute = new RouteCollection();
        allCar = new CarCollection();
        carCollection = new CarCollection();
    }

    // JourneyCollection
    public JourneyCollection getJourneyCollection() {
        return journeyCollection;
    }
    public void addJourney(Journey journey) {
        journeyCollection.addJourney(journey);
    }

    // CarCollection
    public CarCollection getCarCollection(){
        return carCollection;
    }
    public void addCarToCollecton(Car car) {
        carCollection.addCar(car);
    }
    public Car getCarFromCollection(int index) {
        return carCollection.getCar(index);
    }
    public void changeCarInCollection(Car car, int indexOfChanging) {
        carCollection.changeCar(car, indexOfChanging);
    }
    public void removeCarFromCollection(int index) {
        carCollection.remove(index);
    }
    public Car getLastCarInList() {
        return carCollection.getLastCar();
    }
    public CarCollection getAllCar() {
        return allCar;
    }
    public void addCarToAllCar(Car car) {
        allCar.addCar(car);
    }

    // RouteCollection
    public RouteCollection getRouteCollection() {
        return routeCollection;
    }
    public void addRouteToCollecton(Route route) {
        routeCollection.addRoute(route);
    }
    public Route getRouteFromCollection(int index) {
        return routeCollection.getRoute(index);
    }
    public void removeRouteFromCollection(int index) {
        routeCollection.remove(index);
    }
    public RouteCollection getAllRoute() {
        return allRoute;
    }
    public void addRouteToAllRoute(Route route) {
        allRoute.addRoute(route);
    }
    public void changeRouteInCollection(Route route, int indexOfChanging) {
        routeCollection.changeRoute(route, indexOfChanging);
    }
    public Route getLastRoute() {
        return allRoute.getLastRoute();
    }
}
