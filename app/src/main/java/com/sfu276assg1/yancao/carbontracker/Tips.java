package com.sfu276assg1.yancao.carbontracker;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Help to generate tips.
 */

public class Tips {
    private final static int CARTIPS = 0;
    private final static int ELECTRICITYTIPS = 1;
    private final static int GASTIPS = 2;
    private final static int NOTIPS = 3;

    private String date;

    public Tips() {
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public int showWhichTip(JourneyCollection journeyCollection, BillCollection billCollection){
        double total_car_carbon = journeyCollection.getJourneyCarbonInOneDay(date);
        double total_util_carbon = 0.0;
        double total_elect_carbon = 0.0;
        double total_gas_carbon = 0.0;

        Bill bill = billCollection.getLastBill();
        if(bill==null&&total_car_carbon==0){
            return NOTIPS;
        }
        if(bill!=null) {
            total_util_carbon=bill.getTotalCarbonEmission();
            total_elect_carbon=bill.getElectricityCarbonEmission();
            total_gas_carbon=bill.getGasCarbonEmission();
        }

        if(total_car_carbon >= total_util_carbon){
            return CARTIPS;
        }
        else{
            if(total_elect_carbon > total_gas_carbon){
                return ELECTRICITYTIPS;
            }
            else{
                return GASTIPS;
            }
        }
    }

    public String generateCarTip(Context context, JourneyCollection journeyCollection, int index) {
        String[] tooMuchCar = {context.getResources().getString(R.string.CAR_TIP_1),
                context.getResources().getString(R.string.CAR_TIP_2),
                context.getResources().getString(R.string.CAR_TIP_3),
                context.getResources().getString(R.string.CAR_TIP_4),
                context.getResources().getString(R.string.CAR_TIP_5),
                context.getResources().getString(R.string.CAR_TIP_6),
                context.getResources().getString(R.string.CAR_TIP_7)};
        int arraySize = tooMuchCar.length;
        Double journeyCarNum = 0.0;
        String journeyCarbon = "";
        String journeyNum = Integer.toString(CarbonModel.getInstance().getJourneyCollection().countJourneyInOneDate(date));
        String tip = "";
        if(CarbonModel.getInstance().getUnitChoice()==1){
            journeyCarNum = journeyCollection.getJourneyCarbonInOneDay(date)*10/10;
            journeyCarbon = String.format("%.2f",journeyCarNum);
            tip = context.getResources().getString(R.string.CAR_TIP_PART1)+ " "+journeyNum +" "+
                    context.getResources().getString(R.string.CAR_TIP_PART2)+ " "
                    +journeyCarbon+context.getResources().getString(R.string.kg)+". "+tooMuchCar[index%arraySize];
        }
        else {
            journeyCarNum = journeyCollection.getJourneyCarbonInOneDayTreeYear(date) * 10/10;
            journeyCarbon = String.format("%.2f",journeyCarNum);
            tip = context.getResources().getString(R.string.CAR_TIP_PART1) + " " + journeyNum + " " +
                    context.getResources().getString(R.string.CAR_TIP_PART2) + " "
                    + journeyCarbon + " "+
                    context.getResources().getString(R.string.tree)+". "
                    + tooMuchCar[index % arraySize];
        }
        return tip;
    }

    public String generateElectricityTip(Context context, BillCollection billCollection, int index) {
        String[] tooMuchElectricity = {context.getResources().getString(R.string.ELE_TIP_1),
                context.getResources().getString(R.string.ELE_TIP_2),
                context.getResources().getString(R.string.ELE_TIP_3),
                context.getResources().getString(R.string.ELE_TIP_4),
                context.getResources().getString(R.string.ELE_TIP_5),
                context.getResources().getString(R.string.ELE_TIP_6),
                context.getResources().getString(R.string.ELE_TIP_7)};
        int arraySize = tooMuchElectricity.length;
        Double elecCarNum;
        String electricityCarbon = "";
        String tip = "";
        if(CarbonModel.getInstance().getUnitChoice()==1){
            elecCarNum = billCollection.getLastBill().getElectricityCarbonEmission();
            electricityCarbon = String.format("%.2f",elecCarNum);
            tip = context.getResources().getString(R.string.ELE_TIP_PART1)+" " +
                    electricityCarbon+" "+ context.getResources().getString(R.string.kg)+". "
                    +tooMuchElectricity[index%arraySize];
        }
        else{
            elecCarNum = billCollection.getLastBill().getElectricityCarbonTreeYear();
            electricityCarbon = String.format("%.2f",elecCarNum);
            tip = context.getResources().getString(R.string.ELE_TIP_PART1)+" " +
                    electricityCarbon+" "+ context.getResources().getString(R.string.tree)+". "
                    +tooMuchElectricity[index%arraySize];
        }
        return tip;
    }

    public String generateGasTip(Context context, BillCollection billCollection, int index) {
        String[] tooMuchGas = {
                context.getResources().getString(R.string.GAS_TIP_1),
                context.getResources().getString(R.string.GAS_TIP_2),
                context.getResources().getString(R.string.GAS_TIP_3),
                context.getResources().getString(R.string.GAS_TIP_4),
                context.getResources().getString(R.string.GAS_TIP_5),
                context.getResources().getString(R.string.GAS_TIP_6),
                context.getResources().getString(R.string.GAS_TIP_7),
                context.getResources().getString(R.string.GAS_TIP_8),
                context.getResources().getString(R.string.GAS_TIP_9)};
        int arraySize = tooMuchGas.length;
        Double gasCarNum = 0.0;
        String gasCarbon = "";
        String tip = "";
        if(CarbonModel.getInstance().getUnitChoice()==1){
            gasCarNum = billCollection.getLastBill().getGasCarbonEmission();
            gasCarbon = String.format("%.2f",gasCarNum);
            tip = context.getResources().getString(R.string.ELE_TIP_PART1)+" " +
                    gasCarbon+" "+ context.getResources().getString(R.string.kg)+". "
                    +tooMuchGas[index%arraySize];
        }
        else{
            gasCarNum = billCollection.getLastBill().getGasCarbonTreeYear();
            gasCarbon = String.format("%.2f",gasCarNum);
            tip = context.getResources().getString(R.string.GAS_TIP_PART1)+" " +
                    gasCarbon+" "+ context.getResources().getString(R.string.tree)+". "
                    +tooMuchGas[index%arraySize];
        }
        return tip;
    }
}

