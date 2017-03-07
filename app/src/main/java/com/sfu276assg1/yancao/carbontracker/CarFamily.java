package com.sfu276assg1.yancao.carbontracker;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by yancao on 2017-03-06.
 */

public class CarFamily {
    private ArrayList<Car> cars = new ArrayList<>();

    public CarFamily(ArrayList<Car> cars){
        this.cars = cars;
    }

    public ArrayList<String> defaultForGetMake() {
        ArrayList<String> makeList = new ArrayList<String>();
        makeList.add("");
        ArrayList<String> tmp = getMake();
        for(String make:tmp) {
            makeList.add(make);
        }
        return makeList;
    }
    public ArrayList<String> getMake(){
        ArrayList<String> makeList = new ArrayList<String>();
        HashSet<String> makeSet  = new HashSet<String>();
        for(Car car:cars){
            String theMake = car.getMake();
            if(!makeSet.contains(theMake)){
                makeSet.add(theMake);
                makeList.add(theMake);
            }

        }
        return makeList;
    }

    public ArrayList<String> getModel(String make){
        ArrayList<String> modelList = new ArrayList<String>();
        HashSet<String> modelSet  = new HashSet<String>();
        for(Car car:cars){
            if(car.getMake().equals(make)){
                String theModel = car.getModel();
                if(!modelSet.contains(theModel)){
                    modelSet.add(theModel);
                    modelList.add(theModel);
                }
            }
        }
        return modelList;
    }

    public ArrayList<String> getYear(String make, String model){
        ArrayList<String> yearList = new ArrayList<String>();
        HashSet<String> yearSet  = new HashSet<String>();
        for(Car car:cars){
            if(car.getMake().equals(make)&&car.getModel().equals(model)){
                String theYear = car.getYear();
                if(!yearSet.contains(theYear)){
                    yearSet.add(theYear);
                    yearList.add(theYear);
                }
            }
        }
        return yearList;
    }

    public ArrayList<Integer> getEmission(String make, String model, String year) {
        ArrayList<Integer> emission = new ArrayList<Integer>();
        for (Car car : cars) {
            if (car.getMake().equals(make) && car.getModel().equals(model) && car.getYear().equals(year) ){
                int highwayE = car.getHighwayE();
                int cityE = car.getCityE();
                emission.add(highwayE);
                emission.add(cityE);
                break;
            }
        }
        return emission;
    }

}
