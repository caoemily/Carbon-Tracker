package com.sfu276assg1.yancao.carbontracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vu on 2017-03-22.
 */

public class DataForOneMonth {
    private int month;
    private int year;
    private float totalCarbonForCar;
    private float totalCarbonForPublic;
    private float totalCarbonUtilities;

    public DataForOneMonth(int month, int year) {
        this.month = month;
        this.year = year;
        totalCarbonForCar = 0;
        totalCarbonForPublic = 0;
        totalCarbonUtilities = 0;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public String getLastDayOfMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String firstDateOfMonth = "" + year + "-" + String.format("%02d", month) + "-" + 01;
        Calendar calendar = Calendar.getInstance();
        try {
            Date convertedDate = dateFormat.parse(firstDateOfMonth);
            calendar.setTime(convertedDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }catch(ParseException e) {}
        String formatted = dateFormat.format(calendar.getTime());
        return formatted;
    }

    public float getTotalCarbonForCar() {
        return totalCarbonForCar;
    }

    public void setTotalCarbonForCar(float totalCarbonForCar) {
        this.totalCarbonForCar = totalCarbonForCar;
    }

    public float getTotalCarbonForPublic() {
        return totalCarbonForPublic;
    }

    public void setTotalCarbonForPublic(float totalCarbonForPublic) {
        this.totalCarbonForPublic = totalCarbonForPublic;
    }

    public float getTotalCarbonUtilities() {
        return totalCarbonUtilities;
    }

    public void setTotalCarbonUtilities(float totalCarbonUtilities) {
        this.totalCarbonUtilities = totalCarbonUtilities;
    }
}
