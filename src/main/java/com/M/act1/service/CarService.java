package com.M.act1.service;

import org.springframework.stereotype.Service;
import com.M.act1.models.Car;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    // Example: In-memory list, replace with repository later
    private final List<Car> cars = new java.util.ArrayList<>();
    private Long nextId = 1L;

    public List<Car> getAllCars() {
        return cars;
    }

    public void addCar(Car car) {
        car.setCarId(nextId++);
        cars.add(car);
    }

    public Optional<Car> getCarById(Long id) {
        return cars.stream().filter(c -> c.getCarId().equals(id)).findFirst();
    }

    public void updateCar(Car car) {
        getCarById(car.getCarId()).ifPresent(existing -> {
            existing.setLicensePlateNumber(car.getLicensePlateNumber());
            existing.setMake(car.getMake());
            existing.setModel(car.getModel());
            existing.setYear(car.getYear());
            existing.setColor(car.getColor());
            existing.setBodyType(car.getBodyType());
            existing.setEngineType(car.getEngineType());
            existing.setTransmission(car.getTransmission());
        });
    }

    public void deleteCarById(Long id) {
        cars.removeIf(c -> c.getCarId().equals(id));
    }
}
