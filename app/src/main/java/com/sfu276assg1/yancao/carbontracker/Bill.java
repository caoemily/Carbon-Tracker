package com.sfu276assg1.yancao.carbontracker;

/**
 * Store information about a single bill
 */

public class Bill {
    private String startDate;
    private String endDate;
    private double electricity;
    private double gas;
    private int people;

    public Bill(String startDate, String endDate, double electricity, double gas, int people) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.electricity = electricity;
        this.gas = gas;
        this.people = people;
    }
    public Bill(){
        this.startDate = " ";
        this.endDate = " ";
        this.electricity = 0;
        this.gas = 0;
        this.people = 0;
    }

    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public double getGas() {
        return gas;
    }
    public double getElectricity() {
        return electricity;
    }
    public int getPeople() {
        return people;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setGas(double gas) {
        this.gas = gas;
    }
    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }
    public void setPeople(int people) {
        this.people = people;
    }
}

