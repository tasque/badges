package org.badges.security;

import org.badges.db.Employee;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequestContext {

    private static final ThreadLocal<Employee> currentEmployee = new ThreadLocal<>();


    public Employee getCurrentEmployee() {
        return Optional.ofNullable(currentEmployee.get())
                .orElse(new Employee().setId(1L));
    }

    void setCurrentEmplyee(Employee employee) {
        currentEmployee.set(employee);
    }

    void clear() {
        currentEmployee.remove();
    }

}
