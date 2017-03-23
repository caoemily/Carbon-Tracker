package com.sfu276assg1.yancao.carbontracker;

/**
 * Store information about a single route
 */

public class Route {
    private String type;
    private String rName;
    private double distance;
    private double lowEDis;
    private double highEDis;

    public Route() {
        this.type = " ";
        this.rName = " ";
        this.distance = 0;
        this.lowEDis = 0;
        this.highEDis = 0;

    }

    public double getHighEDis() {
        return highEDis;
    }

    public void setHighEDis(double highEDis) {
        if(highEDis < 0){
            throw new IllegalArgumentException();
        }
        else{
            this.highEDis = highEDis;
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

    public double getLowEDis() {
        return lowEDis;
    }

    public void setLowEDis(double lowEDis) {
        if(lowEDis < 0){
            throw new IllegalArgumentException();
        }
        else{
            this.lowEDis = lowEDis;
        }
    }

    public String getName() {
        return rName;
    }

    public void setName(String rName) {
        this.rName = rName;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Route route) {
        return (route.getName().equals(this.rName)) &&
                (route.getDistance()== this.distance) &&
                (route.getLowEDis()== this.lowEDis) &&
                (route.getHighEDis() == this.highEDis)&&
                (route.getType().equals(this.type));
    }
}
