package com.M.act1;

import com.M.act1.repository.CarRepository;
import com.M.act1.models.Car;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbTester implements CommandLineRunner {

    private final CarRepository carRepository;

    // Constructor injection
    public DbTester(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create a new car
        Car car = new Car();
        car.setLicensePlateNumber("MS8523");
        car.setMake("Toyota");
        car.setModel("Civic");
        car.setYear(2018);
        car.setColor("Pink");
        car.setBodyType("Sedan");
        car.setEngineType("Gasoline");
        car.setTransmission("CVT");

        // Save the car to DB
        carRepository.save(car);

            // Print all cars from DB
        carRepository.findAll().forEach(carRow -> {
            System.out.println(
                    carRow.getLicensePlateNumber() + " " +
                            carRow.getMake() + " " +
                            carRow.getModel() + " " +
                            carRow.getYear() + " " +
                            carRow.getColor() + " " +
                            carRow.getBodyType() + " " +
                            carRow.getEngineType() + " " +
                            carRow.getTransmission()
            );
        });
    }
}
