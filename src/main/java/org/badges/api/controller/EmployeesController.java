package org.badges.api.controller;

import lombok.RequiredArgsConstructor;
import org.badges.db.Employee;
import org.badges.db.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeesController {

    private final EmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> getById(@RequestParam(defaultValue = "") String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }
}
