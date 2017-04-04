package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage a collection of cars.
 */
public class CarCollection {
    private List<Car> cars = new ArrayList<>();

    public void addCar (Car car) {
        cars.add(car);
    }

    public int countCars() {
        return cars.size();
    }

    public Car getCar(int index) {
        validateIndexWithException(index);
        return cars.get(index);
    }

    public List<Car> getCollection() {
        return this.cars;
    }

    public void changeCar (Car car, int indexofCarEditing) {
        validateIndexWithException(indexofCarEditing);
        cars.remove(indexofCarEditing);
        cars.add(indexofCarEditing, car);
    }

    public void remove (int index) {
        cars.remove(index);
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countCars()) {
            throw new IllegalArgumentException();
        }
    }
}