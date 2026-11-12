package com.example.employeemanagement.config;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("dev")
@ConditionalOnProperty(
        prefix = "app.seed",
        name = "employees",
        havingValue = "true"
)
public class EmployeeDataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public EmployeeDataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        createEmployeeIfMissing(
                "alice.johnson@example.com", "Alice", "Johnson",
                "Human Resources", "65000.00"
        );
        createEmployeeIfMissing(
                "mark.williams@example.com", "Mark", "Williams",
                "Engineering", "82000.00"
        );
        createEmployeeIfMissing(
                "priya.sharma@example.com", "Priya", "Sharma",
                "Finance", "71000.00"
        );
        createEmployeeIfMissing(
                "david.lee@example.com", "David", "Lee",
                "Marketing", "68000.00"
        );
        createEmployeeIfMissing(
                "sophia.martin@example.com", "Sophia", "Martin",
                "Product", "79000.00"
        );
    }

    private void createEmployeeIfMissing(
            String email,
            String firstName,
            String lastName,
            String department,
            String salary
    ) {
        if (employeeRepository.existsByEmail(email)) {
            return;
        }

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setDepartment(department);
        employee.setSalary(new BigDecimal(salary));
        employeeRepository.saveAndFlush(employee);
    }
}
