package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;

public class CarCollection {

    private List<Car> cars = new ArrayList<>();


    // Jane, can you please add this method to your java class? Useful for CarbonModel use ^^
    public Car getLastOne(){
        if(cars.size()==0) return null;
        else return cars.get(cars.size()-1);
    }
}
