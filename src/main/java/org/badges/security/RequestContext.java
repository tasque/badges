package org.badges.security;

import org.badges.db.Company;
import org.badges.db.Employee;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequestContext {

    private static final ThreadLocal<Employee> currentEmployee = new ThreadLocal<>();

    public Company getCurrentTenant() {
        Employee employee = getCurrentEmployee();
        if (employee != null) {
            return employee.getCompany();
        }
        return null;
    }

    public Employee getCurrentEmployee() {
        return Optional.ofNullable(currentEmployee.get())
                .orElse(new Employee().setId(1L)
                        .setCompany(new Company().setId(1)));
    }

    void setCurrentEmplyee(Employee employee) {
        currentEmployee.set(employee);
    }

    void clear() {
        currentEmployee.remove();
    }

}
