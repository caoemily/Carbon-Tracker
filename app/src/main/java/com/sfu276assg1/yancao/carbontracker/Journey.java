package com.sfu276assg1.yancao.carbontracker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Store information about a single journey
 */

public class Journey implements Serializable {

    private String date;
    private Car car;
    private Route route;

    public Journey (Car car, Route route) {
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.car = car;
        this.route = route;
    }

    public Journey (String date) {
        if (date == null){
            this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        else {
            this.date = date;
        }
    }

    public Journey (Route route){
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.route = route;
        Car car = new Car();
        this.car = car;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Car getCar() {
        return this.car;
    }

    public void changeCar(Car car) {
        this.car = car;
    }

    public Route getRoute(){
        return this.route;
    }

    public void changeRoute(Route route) {
        this.route = route;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String calculateCarbon() {
        double carbonEmission = 0;
        if(this.route.getType().equals("drive")){
            double highway = this.route.getLowEDis() * 0.621371;
            double city = this.route.getHighEDis() * 0.621371;
            double gasUsed = highway /this.car.getHighwayE()  + city / this.car.getHighwayE();
            if (this.car.getFuelType().equals("Gasoline") || this.car.getFuelType().equals("Regular") || this.car.getFuelType().equals("Premium")) {
                carbonEmission = 8.89 * gasUsed;
            }
            else if (this.car.getFuelType().equals("Diesel")) {
                carbonEmission = 10.16 * gasUsed;
            }
            else if (this.car.getFuelType().equals("Electric")){
                carbonEmission = 0 * gasUsed;
            }
        }
        else if(this.route.getType().equals("public")){
            carbonEmission = (route.getLowEDis()*50 + route.getHighEDis()*89)/1000;
        }
        return String.format("%.2f", carbonEmission);
    }

    public String getJourneyDes(){
        String des = "";
        String carName = this.car.getNickname();
        String routeName = this.route.getName();
        if(routeName.equals(" ")) {
            routeName = "no name";
        }
        String distance = Double.toString(this.route.getDistance());
        if(this.route.getType().equals("drive")){
            des = "Current Journey Info: Emission: " + calculateCarbon() + " kg"+
                    "; Car: " + carName + "; Route: " + routeName + "; Distance: " + distance + " km.";
        }
        else if (this.route.getType().equals("public")){
            des = "Current Journey Info: Emission: " + calculateCarbon() + " kg"+
                    "; Public Transit - Route: " + routeName + "; Distance: " + distance + " km.";
        }
        else
        {
            des = "Current Journey Info: Emission: 0kg; Bike/Walk - Route: " + routeName + "; Distance: " + distance + " km.";
        }
        return des;
    }

}
