package com.prelim.exam;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // ✅ List all employees
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // ✅ Get one employee by ID
    public Employee getEmployee(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    // ✅ Add a new employee (with email check)
    public Employee createEmployee(Employee employee) {
        if (repository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        return repository.save(employee);
    }

    // ✅ Update employee
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee existing = getEmployee(id);
        existing.setName(employeeDetails.getName());
        existing.setEmail(employeeDetails.getEmail());
        return repository.save(existing);
    }

    // ✅ Delete employee
    public void deleteEmployee(Long id) {
        repository.deleteById(id);
    }
}
