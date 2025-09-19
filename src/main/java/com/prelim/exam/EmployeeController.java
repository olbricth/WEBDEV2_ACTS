package com.prelim.exam;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // List employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", service.getAllEmployees());
        return "employee-list"; // 👈 matches your employee list page
    }

    // Show add form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee-form"; // 👈 your add form page
    }

    // Handle add
    @PostMapping
    public String addEmployee(@Valid @ModelAttribute Employee employee,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employee-form";
        }
        service.createEmployee(employee);
        return "redirect:/employees";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = service.getEmployee(id);
        model.addAttribute("employee", employee);
        return "employee-form"; // 👈 reuse the same form.html
    }

    // Handle update
    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute Employee employee,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employee-form";
        }
        service.updateEmployee(id, employee);
        return "redirect:/employees";
    }

    @GetMapping("/login")
    public String handleLoginRedirect() {
        return "redirect:/employees";
    }

    // Handle delete
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return "redirect:/employees";
    }
}
