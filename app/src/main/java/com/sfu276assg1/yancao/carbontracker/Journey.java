package com.sfu276assg1.yancao.carbontracker;

import java.io.Serializable;


public class Journey implements Serializable {

    private String date;
    private String routeName;
    private float distance;
    private String carName;
    private float numCarbon;

    public Journey(String date, String routeName, float distance, String carName) {
        this.date = date;
        this.routeName = routeName;
        this.distance = distance;
        this.carName = carName;
    }

    public String getDate() {
        return date;
    }

    public String getRouteName() {
        return routeName;
    }

    public float getDistance() {
        return distance;
    }

    public String getCarName() {
        return carName;
    }

    public void setNumCarbon(int numCarbon) {
        if (numCarbon < 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.numCarbon = numCarbon;
        }
    }

    public float getNumCarbon() {
        return numCarbon;
    }
}
