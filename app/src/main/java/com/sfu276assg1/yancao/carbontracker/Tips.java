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
        if(bill!=null) {
            total_util_carbon=bill.getTotalCarbonEmission();
            total_elect_carbon=bill.getElectricityCarbonEmission();
            total_gas_carbon=bill.getGasCarbonEmission();
        }

        if(total_car_carbon > total_util_carbon){
            return CARTIPS;
        }
        else{
            if(total_elect_carbon>total_gas_carbon){
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
                context.getResources().getString(R.string.CAR_TIP_7),};
        int arraySize = tooMuchCar.length;
        Double journeyCarNum = 0.0;
        String journeyCarbon = "";
        String journeyNum = Integer.toString(CarbonModel.getInstance().getJourneyCollection().countJourneyInOneDate(date));
        String tip = "";
        if(CarbonModel.getInstance().getUnitChoice()==1){
            journeyCarNum = journeyCollection.getJourneyCarbonInOneDay(date)*10/10;
            journeyCarbon = Double.toString(journeyCarNum);
            tip = context.getResources().getString(R.string.CAR_TIP_PART1)+ " "+journeyNum +" "+
                    context.getResources().getString(R.string.CAR_TIP_PART2)+ " "
                    +journeyCarbon+" kg. "+tooMuchCar[index%arraySize];
        }
        else {
            journeyCarNum = journeyCollection.getJourneyCarbonInOneDayTreeYear(date) * 10/10;
            journeyCarbon = Double.toString(journeyCarNum);
            tip = context.getResources().getString(R.string.CAR_TIP_PART1) + " " + journeyNum + " " +
                    context.getResources().getString(R.string.CAR_TIP_PART2) + " "
                    + journeyCarbon + " tree year. " + tooMuchCar[index % arraySize];
        }
        return tip;
    }

    public String generateElectricityTip(BillCollection billCollection, int index) {
        String[] tooMuchElectricity = {"Turn off the lights when you can!", "Install Compact Fluorescent Bulbs to save energy!", "Wash your clothes with cold water!",
                "Set your refrigerator to the optimal temperature!", "Turn off your lights when you're not using it!", "Wash and dry full loads!", "Cut your heating needs!",
                "Unplug unnecessary appliances!", "Run your dishwasher only with a full load!"};
        int arraySize = tooMuchElectricity.length;
        Double elecCarNum = 0.0;
        String electricityCarbon = "";
        String tip = "";
        if(CarbonModel.getInstance().getUnitChoice()==1){
            elecCarNum = billCollection.getLastBill().getElectricityCarbonEmission();
            electricityCarbon = Double.toString(elecCarNum);
            tip = "According to your last bill, the amount of carbon emission by electricity daily is: "+electricityCarbon+" kg. "+tooMuchElectricity[index%arraySize];
        }
        else{
            elecCarNum = billCollection.getLastBill().getElectricityCarbonTreeYear();
            electricityCarbon = Double.toString(elecCarNum);
            tip = "According to your last bill, the amount of carbon emission by electricity daily is: "+electricityCarbon+" tree year. "+tooMuchElectricity[index%arraySize];
        }
        return tip;
    }

    public String generateGasTip(BillCollection billCollection, int index) {
        String[] tooMuchGas = {"Insulate your house!", "Take quicker showers!", "Close off doors and vents in unused rooms to conserve heat within your home!",
                "Upgrade your heating equipments!", "Don't let the water run!", "Install a programmable thermostat!", "Seal air leaks with caulk!", "Replace Any old Natural Gas Heaters!" };
        int arraySize = tooMuchGas.length;
        Double gasCarNum = 0.0;
        String gasCarbon = "";
        String tip = "";
        if(CarbonModel.getInstance().getUnitChoice()==1){
            gasCarNum = billCollection.getLastBill().getGasCarbonEmission();
            gasCarbon = Double.toString(gasCarNum);
            tip = "According to your last bill, the amount of carbon emission by natural gas daily is: "+gasCarbon+" kg. "+tooMuchGas[index%arraySize];
        }
        else{
            gasCarNum = billCollection.getLastBill().getGasCarbonTreeYear();
            gasCarbon = Double.toString(gasCarNum);
            tip = "According to your last bill, the amount of carbon emission by natural gas daily is: "+gasCarbon+" tree year. "+tooMuchGas[index%arraySize];
        }
        return tip;
    }
}


