package com.sfu276assg1.yancao.carbontracker;

public class Route {
    private String rName;
    private double distance;
    private double highwayPer;
    private double cityPer;


    // Set member data based on parameters.
    public Route(double distanceInKm, double highwayPer, double cityPer) {
        if(distanceInKm<=0){
            throw new IllegalArgumentException();
        }
        if(highwayPer<0||highwayPer>100){
            throw new IllegalArgumentException();
        }
        if(cityPer<0||cityPer>100){
            throw new IllegalArgumentException();
        }

        this.rName = " ";
        this.distance = distanceInKm;
        this.highwayPer = highwayPer;
        this.cityPer = cityPer;
    }

    public double getCityPer() {
        return cityPer;
    }

    public void setCityPer(double cityPer) {
        if(cityPer<0||cityPer>100){
            throw new IllegalArgumentException();
        }
        else{
            this.cityPer=cityPer;
        }
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if(distance<=0){
            throw new IllegalArgumentException();
        }
        else {
            this.distance = distance;
        }
    }

    public double getHighwayPer() {
        return highwayPer;
    }

    public void setHighwayPer(double highwayPer) {
        if(highwayPer<0||highwayPer>100){
            throw new IllegalArgumentException();
        }
        else{
            this.highwayPer=highwayPer;
        }
    }

    public String getName() {
        return rName;
    }

    public void setName(String rName) {
        if(rName.isEmpty()){
            throw new IllegalArgumentException();
        }
        else{
            this.rName = rName;
        }
    }


    public void setRoute(Route route){
        this.setName(route.getName());
        this.setDistance(route.getDistance());
        this.setHighwayPer(route.getHighwayPer());
        this.setCityPer(route.getCityPer());
    }

    public String getSingleRouteDes() {
        String descriptions = "";
        descriptions += this.getName() + " - " + this.getDistance() + "km" + " - "
                    + this.getHighwayPer() + "%" + " - " + this.getCityPer() + "%";
        return descriptions;
    }

}