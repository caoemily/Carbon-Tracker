package com.sfu276assg1.yancao.carbontracker;

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();
    private RouteCollection routeCollection;

    public static CarbonModel getInstance() {
        return ourInstance;
    }
    private CarbonModel() {
        routeCollection = new RouteCollection();
    }


    public RouteCollection getRouteCollection(){
        return routeCollection;
    }
    public void addRouteToCollecton(Route route){ routeCollection.addRoute(route);}
    public void changeRouteInCollection(Route route, int indexOfChanging) {routeCollection.changeRoute(route, indexOfChanging);}
    public Route getRouteFromCollection(int index){
        return routeCollection.getRoute(index);
    }
    public Route getLastRouteFromCollection(){
        int index = routeCollection.countRoutes()-1;
        if(index>=0){
            return routeCollection.getRoute(index);
        }
        else {return null;}

    }
    public void removeLastRouteFromCollection(){
        int index = routeCollection.countRoutes()-1;
        if(index>=0) {
            routeCollection.remove(index);
        }
        else return;
    }
    public void removeRouteFromCollection(int index){
        routeCollection.remove(index);
    }
}
