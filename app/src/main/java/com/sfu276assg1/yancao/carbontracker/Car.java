package com.sfu276assg1.yancao.carbontracker;

public class Car {

    private String nickname, make, model, year;

    //set member data based on parameter
    public Car()
    {
        nickname = " ";
        make = " ";
        model = " ";
        year = " ";
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