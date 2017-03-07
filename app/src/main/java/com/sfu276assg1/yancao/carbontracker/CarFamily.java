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

    public void setCars(ArrayList<Car> cars){
        this.cars = cars;
    }

//    public CarFamily(Context context){
        //read in the cvs file. Right now data is fake.
//        char make = 'a';
//        char model = 'm';
//        char year = '1';
//        int highwayE = 10;
//        int cityE = 20;
//        for(int i=0; i<10; i++){
//            Car theCar = new Car(String.valueOf(make),String.valueOf(model),
//                    String.valueOf(year), String.valueOf(highwayE++),String.valueOf(cityE++));
//            Car anotherCar = new Car(String.valueOf(make),String.valueOf(model),
//                    String.valueOf(year++), String.valueOf(highwayE++),String.valueOf(cityE++));
//            Car thirdCar = new Car(String.valueOf(make),String.valueOf(model++),
//                    String.valueOf(year), String.valueOf(highwayE++),String.valueOf(cityE++));
//            Car fourCar = new Car(String.valueOf(make),String.valueOf(model++),
//                    String.valueOf(year++), String.valueOf(highwayE++),String.valueOf(cityE++));
//            cars.add(theCar);
//            cars.add(anotherCar);
//            cars.add(thirdCar);
//            cars.add(fourCar);
//            make++;
//        }
//        InputStream is = context.getResources().openRawResource(R.raw.vehicles1);
//        BufferedReader reader = new BufferedReader(
//                new InputStreamReader(is, Charset.forName("UTF-8"))
//        );
//
//        String line = "";
//        try {
//            while ( (line = reader.readLine()) != null) {
//                String[] tokens = line.split(",");
//
//                Car carData = new Car();
//                carData.setMake(tokens[0]);
//                carData.setModel(tokens[1]);
//                carData.setYear(tokens[2]);
//                carData.setCityE(tokens[3]);
//                carData.setHighwayE(tokens[4]);
//                carData.setCylinders(tokens[5]);
//                carData.setDisplacement(tokens[6]);
//                carData.setDrive(tokens[7]);
//                carData.setFuelType(tokens[8]);
//                carData.setTransmission(tokens[9]);
//                cars.add(carData);
//            }
//        } catch (IOException e) {
//            Log.d("ERORRRRRR FILE", "NO GOOOOOODDDDD");
//            e.printStackTrace();
//        }
//    }

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
