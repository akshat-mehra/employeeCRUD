package com.example.demo.employee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class employeeServiceTest {

    @Mock
    private employeeRepository employeeRepository;

    @InjectMocks
    private employeeService employeeService;

    @Test
    void getEmployees() {
        // Define the test case
        List<employee> employeeList = new ArrayList<>();
        employeeList.add(new employee("Akshat Mehra", "akshat@mehra.com"));
        employeeList.add(new employee("Abhishek Sharma", "abhishek@sharma.com"));
        employeeList.add(new employee("Shivam Sharma", "shivam@sharma.com"));

        // Set up mock UserRepository behavior
        when(employeeRepository.findAll()).thenReturn(employeeList);

        // Call the employeeService method being tested
        List<employee> fetchedEmployees = employeeService.getEmployees();

        //Verify the result
        assertEquals(fetchedEmployees, employeeList);

        //testing
        verify(employeeRepository).findAll();

    }

    @Test
    void addNewEmployee() {
        // Define the test case
        employee Employee = new employee("Akshat Mehra", "akshat@mehra.com");

        // Set up mock employeeRepository behavior
        when(employeeRepository.save(Employee)).thenReturn(Employee);

        // Call the employeeService method being tested
        employee savedEmployee = employeeService.addNewEmployee(Employee);

        //Verify the result
        assertEquals(savedEmployee, Employee);

        //testing
        verify(employeeRepository).save(Employee);
    }

    @Test
    void deleteEmployee() {
        // Define the test case
        employee Employee = new employee(1l, "Shivam Sharma", "shivam@sharma.com");
//        employeeRepository.save(Employee);

        // Set up mock employeeRepository behavior
        when(employeeRepository.existsById(Employee.getId())).thenReturn(Boolean.TRUE);

        // Call the employeeService method being tested
        employeeService.deleteEmployee(Employee.getId());


        //Verify the result
//        assertEquals(savedEmployee, Employee);

        //testing
        verify(employeeRepository).deleteById(Employee.getId());
    }


    @Test
    void updateEmployee() {
        // Define the test case
        employee Employee = new employee(1l, "Akshat Mehra", "akshat@mehra.com");

        // Set up mock employeeRepository behavior
        when(employeeRepository.findById(Employee.getId())).thenReturn(Optional.of(Employee));

        // Call the employeeService method being tested
        String newName = "Shivam Sharma";
        String newEmail = "shivam@sharma.com";
        employee updatedEmployee = employeeService.updateEmployee(Employee.getId(),
                newName, newEmail);

        //Verify the result
        assertEquals(newName, updatedEmployee.getName());
        assertEquals(newEmail, updatedEmployee.getEmail());

        //testing
        verify(employeeRepository).findById(Employee.getId());
    }

//    @Test
//    void registerFromFile() {
//    }
//
//    @Test
//    void insertFast() {
//    }
//
//    @Test
//    void insertSlow() {
//    }
}