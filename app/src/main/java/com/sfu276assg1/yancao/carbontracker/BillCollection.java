package com.sfu276assg1.yancao.carbontracker;

import android.util.Log;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class to manage a collection of bills.
 */

public class BillCollection {
    private List<Bill> bills = new ArrayList<>();

    public List<Bill> getBills() {
        return this.bills;
    }

    public void addBill(Bill bill) {
        bills.add(bill);
    }

    public void changeBill(Bill bill, int indexOfJourneyEditing) {
        bills.remove(indexOfJourneyEditing);
        bills.add(indexOfJourneyEditing, bill);
    }

    public int countBills() {
        return bills.size();
    }

    public Bill getBill(int index) {
        return bills.get(index);
    }

    public void remove(int index) {
        bills.remove(index);
    }

    public double getTotalCarbonEmission(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Bill bill : bills) {
            try {
                Date testDate = format.parse(stringDate);
                Date d1 = format.parse(bill.getStartDate());
                Date d2 = format.parse(bill.getEndDate());
                if (!(testDate.before(d1) || testDate.after(d2))) {
                    return bill.getTotalCarbonEmission();
                }
            } catch (ParseException e) {}
        }
        return 0;
    }

    public double getTotalCarbonEmissionTreeYear(String stringDate) {
        return getTotalCarbonEmission(stringDate)/20;
    }

    public double getElectricityCarbonEmission(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Bill bill : bills) {
            try {
                Date testDate = format.parse(stringDate);
                Date d1 = format.parse(bill.getStartDate());
                Date d2 = format.parse(bill.getEndDate());

                if (!(testDate.before(d1) || testDate.after(d2))) {
                    return bill.getElectricityCarbonEmission();
                }
            } catch (ParseException e) {}
        }
        return 0;
    }

    public double getElectricityCarbonEmissionTreeYear(String stringDate) {
        return getElectricityCarbonEmission(stringDate)/20;
    }

    public double getGasCarbonEmission(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Bill bill : bills) {
            try {
                Date testDate = format.parse(stringDate);
                Date d1 = format.parse(bill.getStartDate());
                Date d2 = format.parse(bill.getEndDate());

                if (!(testDate.before(d1) || testDate.after(d2))) {
                    return bill.getGasCarbonEmission();
                }
            } catch (ParseException e) {}
        }
        return 0;
    }

    public double getGasCarbonEmissionTreeYear(String stringDate) {
        return getGasCarbonEmission(stringDate)/20;
    }

    public boolean isBillInPrevious45days (){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i= this.countBills()-1; i>=0; i--){
            String recordDate = this.getBill(i).getRecordDate();
            Date date = new Date();
            Date today = new Date();
            try {
                date = format.parse(recordDate);
            } catch (ParseException e) {}
            if(getDateDiff(date, today, TimeUnit.DAYS)<=45){
                return true;
            }
        }
        return false;
    }

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
