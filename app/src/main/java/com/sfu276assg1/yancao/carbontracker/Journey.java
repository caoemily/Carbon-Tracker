package com.sfu276assg1.yancao.carbontracker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

// Journey class

public class Journey implements Serializable {

    private String date;
    private String routeName;
    private double distance;
    private String carName;
    private double numCarbon;

    public Journey(Car car, Route route) {
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.routeName = route.getName();
        this.distance = route.getDistance();
        this.carName = car.getNickname();
        this.numCarbon = calculateCarbon(car, route);
    }
    public String getDate() {
        return date;
    }
    public void setDate(String jdate){
        if(jdate == null || jdate.length() == 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.date = jdate;
        }
    }
    public String getRouteName() {
        return routeName;
    }
    public void setRouteName(String rname){
        if(rname == null || rname.length() == 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.routeName = rname;
        }
    }

    public double getDistance() {
        return distance;
    }
    public void setDistance(double rdistance) {
        this.distance = rdistance;
    }
    public String getCarName() {
        return carName;
    }
    public void setCarName(String cname) {
        if(cname == null || cname.length() == 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.carName=cname;
        }
    }
    public void setNumCarbon(int numCarbon) {
        if (numCarbon < 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.numCarbon = numCarbon;
        }
    }
    public double getNumCarbon() {
        return numCarbon;
    }
    private double calculateCarbon(Car car, Route route) {
        double highway = route.getHighway() * 0.621371;
        double city = route.getCity() * 0.621371;
        double gasUsed = highway * car.getHighwayE()  + city * car.getHighwayE();
        double carbonEmission = 8.89*gasUsed;
        carbonEmission = (double)Math.round(carbonEmission * 10) / 10;
//        double carbonEmission = -1;
//        if (car.getFuelType() == "Gasoline") {
//            carbonEmission = 8.89 * gasUsed;
//        }
//        else if (car.getFuelType() == "Diesel") {
//            carbonEmission = 10.16 * gasUsed;
//        }
//        else if (car.getFuelType() == "Electric"){
//            carbonEmission = 0 * gasUsed;
//        }
        return carbonEmission;
    }
    public String getJourneyDes(){
        String des = "";
        String emission = ""+getNumCarbon();
        String car = getCarName();
        String route = getRouteName();
        if(route.equals(" ")) route = "no name";
        String distance = ""+getDistance();
        des = "Current Journey Info: Emission: "+emission+
                "; Car: "+car+"; Route: "+route+"; Distance: "+distance+"km.";
        return des;
    }
}
