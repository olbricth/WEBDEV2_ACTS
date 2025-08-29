package com.M.act1;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String viewCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "index";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("car", new Car());
        return "form";
    }

    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car) {
        carService.addCar(car);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return carService.getCarById(id)
                .map(car -> {
                    model.addAttribute("car", car);
                    return "form";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable Long id, @ModelAttribute Car car) {
        car.setCarId(id);
        carService.updateCar(car);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
        return "redirect:/";
    }
}
