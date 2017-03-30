package com.sfu276assg1.yancao.carbontracker;

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
            double total_util_carbon = billCollection.getTotalCarbonEmission(date);
            double total_elect_carbon = billCollection.getElectricityCarbonEmission(date);
            double total_gas_carbon = billCollection.getGasCarbonEmission(date);

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

        public String generateCarTip(JourneyCollection journeyCollection, int index) {
            String[] tooMuchCar = {"Try to take the bike!", "Try to take the public transit", "Try to walk!",
                    "Avoid areas with congested traffic!", "Plan out your journey so you don't get lost and waste fuel!",
                    "Keep your vehicles well maintained!","Don't accelerate unnecessarily!", "Buy a fuel efficient car!"};
            int arraySize = tooMuchCar.length;
            String journeyCarbon = Double.toString(journeyCollection.getJourneyCarbonInOneDay(date));
            String journeyNum = Integer.toString(CarbonModel.getInstance().getJourneyCollection().countJourneyInOneDate(date));
            String tip = "You have gone on "+ journeyNum +" trip(s) today. And the amount of carbon emitted by your car today is: "+journeyCarbon+" kg. "+tooMuchCar[index%arraySize];
            return tip;
        }

        public String generateElectricityTip(BillCollection billCollection, int index) {
            String[] tooMuchElectricity = {"Turn off the lights when you can!", "Install Compact Fluorescent Bulbs to save energy!", "Wash your clothes with cold water!",
                    "Set your refrigerator to the optimal temperature!", "Turn off your lights when you're not using it!", "Wash and dry full loads!", "Cut your heating needs!",
                    "Unplug unnecessary appliances!", "Run your dishwasher only with a full load!"};
            int arraySize = tooMuchElectricity.length;
            String electricityCarbon = Double.toString(billCollection.getElectricityCarbonEmission(date));
            String tip = "The amount of carbon emission by electricity you have produced today is: "+electricityCarbon+" kg. "+tooMuchElectricity[index%arraySize];
            return tip;
        }

        public String generateGasTip(BillCollection billCollection, int index) {
            String[] tooMuchGas = {"Insulate your house!", "Take quicker showers!", "Close off doors and vents in unused rooms to conserve heat within your home!",
                    "Upgrade your heating equipments!", "Don't let the water run!", "Install a programmable thermostat!", "Seal air leaks with caulk!", "Replace Any old Natural Gas Heaters!" };
            int arraySize = tooMuchGas.length;
            String gasCarbon = Double.toString(billCollection.getGasCarbonEmission(date));
            String tip = "The amount of carbon emission by natural gas you have produced today is: "+gasCarbon+" kg. "+tooMuchGas[index%arraySize];
            return tip;
        }

}

