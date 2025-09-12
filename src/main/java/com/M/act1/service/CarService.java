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

    public List<Car> getAllCars() {
        return carRepository.findAll(); // ✅ get all from DB
    }

    public void addCar(Car car) {
        carRepository.save(car); // ✅ save to DB
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id); // ✅ fetch from DB
    }

    public void updateCar(Car car) {
        carRepository.save(car); // ✅ save updated version to DB
    }

    public void deleteCarById(Long id) {
        carRepository.deleteById(id); // ✅ delete from DB
    }
}
