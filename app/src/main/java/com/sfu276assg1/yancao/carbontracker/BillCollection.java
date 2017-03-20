package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
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
}
