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

    public void removeLastJourney() {
        journeys.remove(journeys.size() - 1);
    }

    public int countJourneys() {
        return journeys.size();
    }

    public Journey getJourney(int index) {
        return journeys.get(index);
    }

    public Journey getJourney_id(int id) {
        for (Journey journey : journeys){
            if(journey.getId() == id) {
                return journey;
            }
        }
        return null;
    }

    public Journey getLastJourney(){
        return getJourney(journeys.size() - 1);
    }

    public void removeJourney(int id) {
        int i = 0;
        for (Journey journey : journeys){
            if(journey.getId() == id) {
                journeys.remove(i);
                break;
            }
            i++;
        }
    }

    public List<Journey> getCollection() {
        return this.journeys;
    }

    public void changeCar(Car tempCar, Car car){
        for (Journey journey : journeys){
            if(journey.getCar().equals(tempCar)) {
                journey.changeCar(car);
            }
        }
    }

    public int countJourneyInOneDate(String date){
        int count = 0;
        for(int i = 0; i<countJourneys();i++){
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
        for(int i = 0; i<countJourneys();i++){
            if(date.equals(getJourney(i).getDate())){
                carbonEm += getJourney(i).calculateCarbonDouble();
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
}
