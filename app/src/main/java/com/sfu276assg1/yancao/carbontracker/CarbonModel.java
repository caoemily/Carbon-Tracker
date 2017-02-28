package com.sfu276assg1.yancao.carbontracker;

/**
 * Created by yancao on 2017-02-27.
 */
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
}
