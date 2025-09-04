package com.M.act1.service;

import com.M.act1.exception.ResourceNotFoundException;
import com.M.act1.models.Car;
import com.M.act1.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
    }

    public Car addCar(Car car) {
        if (car.getLicensePlateNumber() == null || car.getLicensePlateNumber().isEmpty()) {
            throw new IllegalArgumentException("License plate number is required");
        }
        if (car.getMake() == null || car.getMake().isEmpty()) {
            throw new IllegalArgumentException("Car make is required");
        }
        // ✅ Add other validations if needed (year > 1886, etc.)
        return carRepository.save(car);
    }



    public Car updateCar(Long id, Car updatedCar) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));

        existingCar.setMake(updatedCar.getMake());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setBodyType(updatedCar.getBodyType());
        existingCar.setEngineType(updatedCar.getEngineType());
        existingCar.setLicensePlateNumber(updatedCar.getLicensePlateNumber());

        return carRepository.save(existingCar);
    }

    public void deleteCarById(Long id) {
        if (!carRepository.existsById(id)) {
            throw new ResourceNotFoundException("Car", id);
        }
        carRepository.deleteById(id);
    }
}
