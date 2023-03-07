package com.example.demo.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface employeeRepository
        extends JpaRepository<employee, Long> {

    // Below method is SQL equivalent of - select * from student where email = ...
    Optional<employee> findEmployeeByEmail(String email);
}
