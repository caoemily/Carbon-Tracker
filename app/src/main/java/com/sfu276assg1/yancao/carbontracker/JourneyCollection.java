package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage a collection of journeys.
 */

public class JourneyCollection {
    private List<Journey> journeys = new ArrayList<>();


    public void addJourney(Journey journey) {
        journeys.add(journey);
    }

    public void changeJourney(Journey journey, int indexOfJourneyEditing) {
        validateIndexWithException(indexOfJourneyEditing);
        journeys.remove(indexOfJourneyEditing);
        journeys.add(indexOfJourneyEditing, journey);
    }

    public void removeLastJourney() {
        journeys.remove(journeys.size() - 1);
    }

    public int countJourneys() {
        return journeys.size();
    }

    public Journey getJourney(int index) {
        return journeys.get(index);
    }

    public Journey getLastJourney(){
        return getJourney(journeys.size() - 1);
    }

    public void remove(int index) {
        journeys.remove(index);
    }

    public void changeCar(Car tempCar, Car car){
        for (Journey journey : journeys){
            if(journey.getCar().equals(tempCar)) {
                journey.changeCar(car);
            }
        }
    }

    public int countJourneyInOneDate(String date){
        int count =0;
        for(int i=0; i<countJourneys();i++){
            if(date.equals(getJourney(i).getDate())){
                count++;
            }
        }
        return count;
    }

    public void changeRoute(Route tempRoute, Route route){
        for (int i = 0; i < countJourneys() - 1; i++) {
            if(getJourney(i).getRoute().equals(tempRoute)) {
                getJourney(i).changeRoute(route);
            }
        }
    }

    public double getJourneyCarbonInOneDay (String date){
        double carbonEm = 0;
        for(int i=0; i<countJourneys();i++){
            if(date.equals(getJourney(i).getDate())){
                carbonEm += Double.parseDouble(getJourney(i).calculateCarbon());
            }
        }
        return carbonEm;
    }

    public double getJourneyCarbonInOneDayTreeYear (String date){
        return (0.0+getJourneyCarbonInOneDay(date))/20.0;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countJourneys()) {
            throw new IllegalArgumentException();
        }

    }

    public ArrayList<String> journeyCollectionDescription() {
        ArrayList<String> journeyDescription = new ArrayList<>();
        for (Journey journey : journeys) {
            journeyDescription.add(journey.getJourneyDes());
        }
        return journeyDescription;
    }
}
