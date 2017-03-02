package com.sfu276assg1.yancao.carbontracker;

public class CarbonModel {
    private static CarbonModel ourInstance = new CarbonModel();
    private RouteCollection routeCollection;
    //private Route currentRoute;
    public static CarbonModel getInstance() {
        return ourInstance;
    }
    private CarbonModel() {
        routeCollection = new RouteCollection();
        //currentRoute = new Route(1,1,1);
    }
    public RouteCollection getRouteCollection(){
        return routeCollection;
    }
    //public Route getCurrentRoute(){return currentRoute;}
}
