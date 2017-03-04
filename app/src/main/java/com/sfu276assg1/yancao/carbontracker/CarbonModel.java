package com.sfu276assg1.yancao.carbontracker;

//Singleton for seprating UI and java classes

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();

    private JourneyCollection journeyCollection;

    private RouteCollection routeCollection;
    private RouteCollection allRoute;

    private CarCollection allCar;
    private CarCollection carCollection;

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

    public void addJourney(Journey journey){journeyCollection.addJourney(journey);}
    public JourneyCollection getAllJourney(){return journeyCollection;}

    public CarCollection getCarCollection(){return carCollection;}
    public CarCollection getAllCar(){return allCar;}
    //more car related func goes here.
    public Car getLastCarInList() {return carCollection.getLastOne();}


    public RouteCollection getRouteCollection(){return routeCollection;}
    public RouteCollection getAllRoute(){return allRoute;}
    public void addRouteToCollecton(Route route){ routeCollection.addRoute(route);}
    public void addRouteToAllRoute(Route route){allRoute.addRoute(route);}
    public void changeRouteInCollection(Route route, int indexOfChanging) {routeCollection.changeRoute(route, indexOfChanging);}
    public Route getRouteFromCollection(int index){
        return routeCollection.getRoute(index);
    }
    public Route getLastRoute(){return allRoute.getLastRoute();}
    public void removeRouteFromCollection(int index){
        routeCollection.remove(index);
    }
}
