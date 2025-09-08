package com.M.act1.controllers;

import com.M.act1.models.Car;
import com.M.act1.service.CarService;
import com.M.act1.exceptions.CarNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String showAddForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("formTitle", "Add New Car");
        return "form";
    }

    @PostMapping("/add")
    public String addCar(@Valid @ModelAttribute Car car, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Add New Car");
            return "form";
        }
        carService.addCar(car);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found."));
        model.addAttribute("car", car);
        model.addAttribute("formTitle", "Edit Car");
        return "form";
    }

    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable Long id, @Valid @ModelAttribute Car car, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Edit Car");
            return "form";
        }
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
