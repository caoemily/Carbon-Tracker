package com.sfu276assg1.yancao.carbontracker;

public class Car {

    private String nickname, make, model, year;
    int highwayE, cityE;

    //set member data based on parameter
    public Car()
    {
        nickname = " ";
        make = " ";
        model = " ";
        year = " ";
        highwayE = 0;
        cityE = 0;
    }

    public Car(String make,String model, String year, String highwayE, String cityE){
        this.nickname = " ";
        this.make = make;
        this.model = model;
        this.year = year;
        this.highwayE = Integer.parseInt(highwayE);
        this.cityE =  Integer.parseInt(cityE);
    }

    public void setHighwayE(int highwayE) {this.highwayE = highwayE;}
    public void setCityE(int cityE){this.cityE = cityE;}
    public int getHighwayE(){return this.highwayE;}
    public int getCityE(){return this.cityE;}

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

    public String getMake() // returns the make
    {
        return make;
    }

    public String getModel() // returns the model
    {
        return model;
    }

    public String getYear() //returns the year
    {
        return year;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }
}