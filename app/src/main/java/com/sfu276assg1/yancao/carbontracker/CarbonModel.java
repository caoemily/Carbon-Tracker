package com.sfu276assg1.yancao.carbontracker;

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();
    private RouteCollection routeCollection;
    private RouteCollection allRoute;

    public static CarbonModel getInstance() {
        return ourInstance;
    }
    private CarbonModel() {

        routeCollection = new RouteCollection();
        allRoute = new RouteCollection();
    }


    public RouteCollection getRouteCollection(){
        return routeCollection;
    }
    public RouteCollection getAllRoute(){return allRoute;}
    public void addRouteToCollecton(Route route){ routeCollection.addRoute(route);}
    public void addRouteToAllRoute(Route route){allRoute.addRoute(route);}
    public void changeRouteInCollection(Route route, int indexOfChanging) {routeCollection.changeRoute(route, indexOfChanging);}
    public Route getRouteFromCollection(int index){
        return routeCollection.getRoute(index);
    }
    public void removeRouteFromCollection(int index){
        routeCollection.remove(index);
    }
}
