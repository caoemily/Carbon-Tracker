package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;


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

    // Useful for integrating with an ArrayAdapter
    public String[] getRouteDescriptions() {
        String[] descriptions = new String[countRoutes()];
        for (int i = 0; i < countRoutes(); i++) {
            Route route = getRoute(i);
            descriptions[i] = route.getName() + " - " + route.getDistance() + "km" + " - "
                    + route.getHighwayPer() + "%" + " - " + route.getCityPer() + "%";
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countRoutes()) {
            throw new IllegalArgumentException();
        }
    }
}
