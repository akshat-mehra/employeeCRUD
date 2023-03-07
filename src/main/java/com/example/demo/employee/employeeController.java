package com.example.demo.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/employee")
public class employeeController {

    private final employeeService employeeService;

    @Autowired
    public employeeController(employeeService employeeService) {
        this.employeeService = employeeService;
    }


    //    @GetMapping("/")
    @Cacheable(value = "employee_gang")
    @GetMapping()
    public List<employee> getEmployees() {
         System.out.println("not using cache, using database");
        return employeeService.getEmployees();
    }

    @Cacheable(value = "employee", key="#id")
    @GetMapping("{id}")
    public Optional<employee> getEmployeeById(@PathVariable Long id) {
         System.out.println("not using cache, using database");
        return employeeService.getEmployeeById(id);
    }

    @CacheEvict(value = "delete", key="#employeeId")
    @DeleteMapping(path = "{employeeId}")
    public void deleteEmployee(@PathVariable("employeeId") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    @CachePut(value = "update", key="#employeeId")
    @PutMapping(path = "{employeeId}")
    public void updateEmployee(@PathVariable("employeeId") Long employeeId,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email) {
        employeeService.updateEmployee(employeeId, name, email);
    }

    @PostMapping
    public void registerNewEmployee(@RequestBody employee employee) {
        employeeService.addNewEmployee(employee);
    }

    @PostMapping(path = "/uploadfile")
    public String registerFromFile(@RequestParam("file")MultipartFile file) {
//        employeeService.addNewEmployee(employee);
        return employeeService.registerFromFile(file);
    }
}
