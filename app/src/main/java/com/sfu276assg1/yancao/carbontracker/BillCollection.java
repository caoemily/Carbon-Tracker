package com.sfu276assg1.yancao.carbontracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public int countBillInOneDate(String s){
        int count = 0;
        for(int i=0; i<countBills();i++){
            if(s.equals(getBill(i).getRecordDate())){
                count++;
            }
        }
        return count;
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
}
