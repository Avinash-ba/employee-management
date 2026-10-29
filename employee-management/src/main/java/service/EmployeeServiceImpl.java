package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DuplicateEmailException;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Convert entity to response DTO
    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }

    // CREATE
    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (employeeRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        Employee employee = new Employee();

        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setEmail(email);
        employee.setDepartment(request.getDepartment().trim());
        employee.setSalary(request.getSalary());

        Employee savedEmployee =
                employeeRepository.saveAndFlush(employee);

        return toResponse(savedEmployee);
    }

    // READ ALL
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ ONE
    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(id)
                );

        return toResponse(employee);
    }

    // UPDATE
    @Override
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request
    ) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(id)
                );

        String email = request.getEmail().trim().toLowerCase();

        if (employeeRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateEmailException(email);
        }

        employee.setFirstName(request.getFirstName().trim());
        employee.setLastName(request.getLastName().trim());
        employee.setEmail(email);
        employee.setDepartment(request.getDepartment().trim());
        employee.setSalary(request.getSalary());

        Employee updatedEmployee =
                employeeRepository.saveAndFlush(employee);

        return toResponse(updatedEmployee);
    }

    // DELETE
    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(id)
                );

        employeeRepository.delete(employee);
    }
}