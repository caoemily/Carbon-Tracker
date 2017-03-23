package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage a collection of routes.
 */

public class RouteCollection {
    private List<Route> routes = new ArrayList<>();

    public void addRoute(Route route) {
        routes.add(route);
    }

    public void changeRoute(Route route, int indexOfRouteEditing) {
        validateIndexWithException(indexOfRouteEditing);
        routes.remove(indexOfRouteEditing);
        routes.add(indexOfRouteEditing, route);
    }

    public void remove (int index){
        routes.remove(index);
    }

    public int countRoutes() {
        return routes.size();
    }

    public Route getRoute(int index) {
        validateIndexWithException(index);
        return routes.get(index);
    }

    public Route getLastRoute(){
        if (routes.size() == 0) {
            return null;
        }
        else {
            return routes.get(routes.size()-1);
        }
    }

    // Useful for integrating with an ArrayAdapter
    public String[] getRouteDescriptions() {
        String[] descriptions = new String[countRoutes()];
        for (int i = 0; i < countRoutes(); i++) {
            Route route = getRoute(i);
            if(route.getType().equals("Drive")){
                descriptions[i] = route.getName() + " - " + route.getDistance() + "km" + " - "
                        +"Highway:" +  route.getLowEDis() + "km" + " - " + "City:" + route.getHighEDis() + "km";
            }
            else if (route.getType().equals("Public Transit")){
                descriptions[i] = route.getName() + " - " + route.getDistance() + "km" + " - "
                        +"Skytrain:" +  route.getLowEDis() + "km" + " - " + "Bus:" + route.getHighEDis() + "km";
            }
            else {
                descriptions[i] = route.getName() + " - " + route.getDistance() + "km" + " - "
                        +"Walk:" +  route.getLowEDis() + "km" + " - " + "Bike:" + route.getHighEDis() + "km";
            }
        }

        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countRoutes()) {
            throw new IllegalArgumentException();
        }
    }
}
