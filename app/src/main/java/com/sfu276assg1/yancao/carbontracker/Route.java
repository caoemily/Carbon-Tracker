package com.sfu276assg1.yancao.carbontracker;

/**
 * Created by yancao on 2017-02-27.
 */

public class Route {
    private String rName;
    private int distance;
    private int highwayPer;
    private int cityPer;
    private boolean saved;


    // Set member data based on parameters.
    public Route(int distanceInKm, int highwayPer, int cityPer) {
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
        this.saved = false;
    }

    public int getCityPer() {
        return cityPer;
    }

    public void setCityPer(int cityPer) {
        if(cityPer<0||cityPer>100){
            throw new IllegalArgumentException();
        }
        else{
            this.cityPer=cityPer;
        }
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        if(distance<=0){
            throw new IllegalArgumentException();
        }
        else {
            this.distance = distance;
        }
    }

    public int getHighwayPer() {
        return highwayPer;
    }

    public void setHighwayPer(int highwayPer) {
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

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}