package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;

//JourneyColletion

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

    public String[] getJourneyDescriptions() {
        String[] descriptions = new String[countJourneys()];
        for (int i = 0; i < countJourneys(); i++) {
            Journey journey = getJourney(i);
            descriptions[i] = journey.getDate() + " - " + journey.getRouteName() + " - " + journey.getDistance() + " - " + journey.getCarName() + " - " + journey.getNumCarbon();
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countJourneys()) {
            throw new IllegalArgumentException();
        }

    }
}
