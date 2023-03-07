package com.example.demo.employee;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class employeeService {

    private final employeeRepository employeeRepository;

    @Autowired
    public employeeService(employeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public employee addNewEmployee(employee employee) {
        Optional<employee> employeeOptional = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        employeeRepository.save(employee);
        return employee;
    }

    public void deleteEmployee(Long employeeId) {
        boolean exists = employeeRepository.existsById(employeeId);
        if (!exists) {
            throw new IllegalStateException(
                    "employee with id " + employeeId + " does not exist");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Transactional
    public employee updateEmployee(Long employeeId, String name, String email) {
        employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalStateException(
                "employee with id " + employeeId + " does not exist"
        )) ;

        if ((name != null) && (name.length() > 0) && !Objects.equals(employee.getName(), name)) {
            employee.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(employee.getEmail(), email)) {
            employee.setEmail(email);
        }

        return employee;
    }


    public String registerFromFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            // Read the file line by line and store the data records in the database
            String line;
            List<employee> employees = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                employee employee = new employee();
                employee.setName(fields[0]);
                employee.setEmail(fields[1]);
                employees.add(employee);
            }

//            employeeRepository.saveAll(employees);
            this.insertFast(employees);
        } catch (IOException e) {
            return "Error reading file";
        }

        return "File uploaded successfully";
    }

    public String insertFast(List<employee> employees) {
        int nThreads = 5;
        int chunkSize = 10;
        List<List<employee>> chunks = new ArrayList<>();
        for (int i = 0; i < employees.size(); i += chunkSize) {
            chunks.add(employees.subList(i, Math.min(i + chunkSize, employees.size())));
        }
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);

        for (List<employee> chunk: chunks) {
            executor.execute(() -> employeeRepository.saveAll(chunk));
        }
        executor.shutdown();

        return "Done!";
    }

    public String insertSlow(List<employee> employees) {
        employeeRepository.saveAll(employees);
        return "Done!";
    }

    public Optional<employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }


}

