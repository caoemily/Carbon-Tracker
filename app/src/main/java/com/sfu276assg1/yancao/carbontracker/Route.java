package com.sfu276assg1.yancao.carbontracker;

/**
 * Store information about a single route
 */

public class Route {
    private String rName;
    private double distance;
    private double highway;
    private double city;

    public Route() {
        this.rName = " ";
        this.distance = 0;
        this.highway = 0;
        this.city = 0;

    }

    public Route(double distanceInKm, double highway, double city) {
        this.rName = " ";
        this.distance = distanceInKm;
        this.highway = highway;
        this.city = city;
    }

    public double getCity() {
        return city;
    }

    public void setCityPer(double city) {
        if(city < 0){
            throw new IllegalArgumentException();
        }
        else{
            this.city = city;
        }
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if(distance < 0){
            throw new IllegalArgumentException();
        }
        else {
            this.distance = distance;
        }
    }

    public double getHighway() {
        return highway;
    }

    public void setHighway(double highway) {
        if(highway < 0){
            throw new IllegalArgumentException();
        }
        else{
            this.highway = highway;
        }
    }

    public String getName() {
        return rName;
    }

    public void setName(String rName) {
        this.rName = rName;

    }

    public String getSingleRouteDes() {
        String descriptions = "";
        descriptions += this.getName() + " - " + this.getDistance() + "km" + " - "
                    + this.getHighway() + "%" + " - " + this.getCity() + "%";
        return descriptions;
    }

    public boolean equals(Route route) {
        return (route.rName == rName) && (route.distance == distance) && (route.highway == highway) && (route.city == city);
    }
}