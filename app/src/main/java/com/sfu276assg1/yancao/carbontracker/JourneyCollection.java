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

    public int countJourneys() {
        return journeys.size();
    }
    public Journey getJourney(int index) {
        validateIndexWithException(index);
        return journeys.get(index);
    }
    public Journey getLastJourney(){
        if(journeys.size()==0) return null;
        else return getJourney(countJourneys()-1);
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countJourneys()) {
            throw new IllegalArgumentException();
        }

    }
}
