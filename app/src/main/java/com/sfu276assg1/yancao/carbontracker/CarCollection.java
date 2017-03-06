package com.sfu276assg1.yancao.carbontracker;

import java.util.ArrayList;
import java.util.List;

public class CarCollection {
    private List<Car> cars = new ArrayList<>();

    public void addCar (Car car) {cars.add(car);}

    public int countCars() {return cars.size();}
    public Car getCar(int index) //gets the car at position index
    {
        validateIndexWithException(index);
        return cars.get(index);
    }

    public void changeCar (Car car, int indexofCarEditing)
    {
        validateIndexWithException(indexofCarEditing);
        cars.remove(indexofCarEditing);
        cars.add(indexofCarEditing, car);
    }

    public Car getLastCar(){
        if(cars.size()==0) return null;
        else {
            return cars.get(cars.size()-1);
        }
    }

    public void remove (int index){
        cars.remove(index);
    }

    public String[] getCarDescription() //for integrating an ArrayAdapter for selecttransmode activity
    {
        String[] descriptions = new String[countCars()];
        for (int i =0; i <countCars(); i++)
        {
            Car car = getCar(i);
            descriptions[i] = car.getNickname() + " - " + car.getMake() + " - "+ car.getModel() + " - " + car.getYear();
        }
        return descriptions;
    }

    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countCars())
        {
            throw new IllegalArgumentException();
        }
    }
}