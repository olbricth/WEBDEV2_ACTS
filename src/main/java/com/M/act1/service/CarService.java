package com.M.act1.service;

import com.M.act1.models.Car;
import com.M.act1.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // Get all cars
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Add a new car
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    // Get car by ID
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    // Update car details
    public Car updateCar(Car car) {
        return carRepository.findById(car.getCarId())
                .map(existing -> {
                    existing.setLicensePlateNumber(car.getLicensePlateNumber());
                    existing.setMake(car.getMake());
                    existing.setModel(car.getModel());
                    existing.setYear(car.getYear());
                    existing.setColor(car.getColor());
                    existing.setBodyType(car.getBodyType());
                    existing.setEngineType(car.getEngineType());
                    existing.setTransmission(car.getTransmission());
                    return carRepository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Car not found with id " + car.getCarId()));
    }

    // Delete a car by ID
    public void deleteCarById(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
        } else {
            throw new RuntimeException("Car not found with id " + id);
        }
    }
}
