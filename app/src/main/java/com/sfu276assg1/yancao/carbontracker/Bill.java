package com.sfu276assg1.yancao.carbontracker;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public double getTotalCarbonEmission(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d1 = format.parse(startDate);
            Date d2 = format.parse(endDate);
            int days = Days.daysBetween(new LocalDate(d1.getTime()), new LocalDate(d2.getTime())).getDays() + 1;

            double electricity_emission = electricity/days/people*900;
            double gas_emission = gas/days/people*56.1;

            return electricity_emission + gas_emission;
        } catch (ParseException e) {}
        return 0;
    }
    public double getElectricityCarbonEmission(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d1 = format.parse(startDate);
            Date d2 = format.parse(endDate);
            int days = Days.daysBetween(new LocalDate(d1.getTime()), new LocalDate(d2.getTime())).getDays() + 1;

            double electricity_emission = electricity/days/people*900;

            return electricity_emission;
        } catch (ParseException e) {}
        return 0;
    }
    public double getGasCarbonEmission(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d1 = format.parse(startDate);
            Date d2 = format.parse(endDate);
            int days = Days.daysBetween(new LocalDate(d1.getTime()), new LocalDate(d2.getTime())).getDays() + 1;

            double electricity_emission = electricity/days/people*900;

            return electricity_emission;
        } catch (ParseException e) {}
        return 0;
    }
}

