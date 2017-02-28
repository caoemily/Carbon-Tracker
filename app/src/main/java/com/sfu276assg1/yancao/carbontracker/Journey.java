package com.sfu276assg1.yancao.carbontracker;

import java.io.Serializable;

/**
 * Created by yancao on 2017-02-27.
 */

public class Journey implements Serializable {
    public Journey(float numCarbon, String carName, String routeName) {
        this.numCarbon = numCarbon;
        this.carName = carName;
        this.routeName = routeName;
    }

    public float getNumCarbon() {
        return numCarbon;
    }

    public String getCarName() {
        return carName;
    }

    public String getRouteName() {
        return routeName;
    }

    @Override
    public String toString() {
        return carName + " " + routeName + " " + numCarbon;
    }

    private float numCarbon;
    private String carName;
    private String routeName;

}
