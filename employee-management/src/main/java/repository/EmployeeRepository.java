package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/** Employee persistence using Hibernate's native Session API. */
@Repository
public class EmployeeRepository {

    private final SessionFactory sessionFactory;

    public EmployeeRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean existsByEmail(String email) {
        return inTransaction(session -> session.createSelectionQuery(
                        "select count(e) from Employee e where e.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0);
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        return inTransaction(session -> session.createSelectionQuery(
                        "select count(e) from Employee e where e.email = :email and e.id <> :id", Long.class)
                .setParameter("email", email)
                .setParameter("id", id)
                .getSingleResult() > 0);
    }

    public Employee saveAndFlush(Employee employee) {
        return inTransaction(session -> {
            if (employee.getId() == null) {
                session.persist(employee);
                session.flush();
                return employee;
            }
            Employee merged = session.merge(employee);
            session.flush();
            return merged;
        });
    }

    public List<Employee> findAll() {
        return inTransaction(session -> session.createSelectionQuery(
                "from Employee", Employee.class).getResultList());
    }

    public Optional<Employee> findById(Long id) {
        return inTransaction(session -> Optional.ofNullable(session.get(Employee.class, id)));
    }

    public void delete(Employee employee) {
        inTransaction(session -> {
            Employee managed = session.contains(employee)
                    ? employee
                    : session.merge(employee);
            session.remove(managed);
            return null;
        });
    }

    private <T> T inTransaction(Function<Session, T> operation) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T result = operation.apply(session);
                transaction.commit();
                return result;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }
}
