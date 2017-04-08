package com.sfu276assg1.yancao.carbontracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Store information about a single journey
 */

public class Journey implements Comparable<Journey> {

    private int id;
    private String date;
    private Car car;
    private Route route;

    public Journey (Car car, Route route) {
        this.id = 0;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.car = car;
        this.route = route;
    }

    public Journey (Route route){
        this.id = 0;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.route = route;
        this.car = new Car();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Journey (String date) {
        if (date == null){
            this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        else {
            this.date = date;
        }
        this.id = 0;
        this.car = new Car();
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

    public double calculateCarbonDouble(){
        double carbonEmission = 0;
        if(this.route.getType().equals("Drive")){
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
        else if(this.route.getType().equals("Public Transit") || this.route.getType().equals("Tránsito público") || this.route.getType().equals("Transport en commun")){
            carbonEmission = (route.getLowEDis()*50 + route.getHighEDis()*89)/1000;
        }
        return carbonEmission;
    }

    public double calculateCarbonTreeYearDouble(){
        return calculateCarbonDouble()/20.0;
    }

    public String calculateCarbon() {
        return String.format("%.2f", calculateCarbonDouble());
    }

    public String calculateCarbonTreeYear(){
        double treeYear = calculateCarbonDouble()/20;
        return String.format("%.2f", treeYear);
    }

    public Date getDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date daTe = format.parse(this.date);
            return daTe;
        } catch (ParseException e) {}
        return null;
    }

    @Override
    public int compareTo(Journey journey) {
        if (getDateTime() == null || journey.getDateTime() == null)
            return 0;
        return getDateTime().compareTo(journey.getDateTime());
    }
}
