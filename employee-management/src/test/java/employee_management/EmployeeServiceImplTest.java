package employee_management;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceImplTest {

    private final EmployeeRepository repository = mock(EmployeeRepository.class);
    private final EmployeeServiceImpl service = new EmployeeServiceImpl(repository);

    @Test
    void createEmployee() {
        when(repository.existsByEmail("jane@example.com")).thenReturn(false);
        when(repository.saveAndFlush(any(Employee.class))).thenAnswer(call -> {
            Employee employee = call.getArgument(0);
            employee.setId(1L);
            return employee;
        });

        var result = service.createEmployee(request());

        assertEquals("Jane", result.getFirstName());
        assertEquals("jane@example.com", result.getEmail());
    }
    @BeforeEach
    void setUp() {
        System.out.println("ACHBCJS");
    }

    @Test
    void readEmployees() {
        Employee employee = employee();
        when(repository.findAll()).thenReturn(List.of(employee));
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        assertEquals("Jane", service.getAllEmployees().get(0).getFirstName());
        assertEquals("Jane", service.getEmployeeById(1L).getFirstName());
    }

    @Test
    void updateEmployee() {
        when(repository.findById(1L)).thenReturn(Optional.of(employee()));
        when(repository.existsByEmailAndIdNot("jane@example.com", 1L))
                .thenReturn(false);
        when(repository.saveAndFlush(any(Employee.class)))
                .thenAnswer(call -> call.getArgument(0));

        var result = service.updateEmployee(1L, request());

        assertEquals("Jane", result.getFirstName());
    }

    @Test
    void deleteEmployee() {
        Employee employee = employee();
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        service.deleteEmployee(1L);

        verify(repository).delete(employee);
    }

    private EmployeeRequest request() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("jane@example.com");
        request.setDepartment("Engineering");
        request.setSalary(new BigDecimal("50000"));
        return request;
    }

    private Employee employee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setEmail("jane@example.com");
        employee.setDepartment("Engineering");
        employee.setSalary(new BigDecimal("50000"));
        return employee;
    }
}
