package com.sfu276assg1.yancao.carbontracker;

// The car class gets all the nickname, make, model, year, highwayE etc.
// They are then stored for easier use throughout the code

public class Car {
    private String nickname, make, model, year;
    private int highwayE, cityE;
    private int cylinders;
    private double displacement;
    private String drive;
    private String fuelType;
    private String transmission;
    private int car_icon;

    //set member data based on parameter
    public Car()
    {
        this.nickname = " ";
        this.make = " ";
        this.model = " ";
        this.year = " ";
        this.highwayE = 0;
        this.cityE = 0;
        this.car_icon = 0;
    }

    public Car(String make,String model, String year, int highwayE, int cityE, String transmission, double displacement, String fuelType){
        this.nickname = " ";
        this.make = make;
        this.model = model;
        this.year = year;
        this.highwayE = highwayE;
        this.cityE = cityE;
        this.transmission = transmission;
        this.displacement = displacement;
        this.fuelType = fuelType;
    }

    public Car(String make, String model, String year, int highwayE, int cityE, String fuelType, String nickname, int car_icon){
        this.make = make;
        this.model = model;
        this.year = year;
        this.highwayE = highwayE;
        this.cityE = cityE;
        this.fuelType = fuelType;
        this.nickname = nickname;
        if (car_icon == 0) {
            this.car_icon = R.drawable.no_icon;
        }
        else {
            this.car_icon = car_icon;
        }
    }

    public void setHighwayE(int highwayE) {
        this.highwayE = highwayE;
    }

    public void setCityE(int cityE){
        this.cityE = cityE;
    }

    public int getHighwayE(){
        return this.highwayE;
    }

    public int getCityE(){
        return this.cityE;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNickname()
    {
        return nickname;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public int getIcon() {
        return car_icon;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public int getCylinders() {
        return cylinders;
    }

    public void setCylinders(int cylinders) {
        this.cylinders = cylinders;
    }

    public double getDisplacement() {
        return displacement;
    }

    public void setDisplacement(double displacement) {
        this.displacement = displacement;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    @Override
    public String toString() {
        return make + ", " + model + ", " + year + ", " + displacement + ", " + transmission;
    }

    public boolean equals(Car car) {
        return (car.nickname == nickname) && (car.make == make) && (car.model == model) && (car.year == year) &&
                (car.highwayE == highwayE) && (car.cityE == cityE) && (car.fuelType == fuelType);
    }
}