package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import com.example.util.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements ApplicationRunner {

  private final EmployeeRepository employeeRepository;
  @PersistenceContext
  public EntityManager entityManager;

  @Autowired
  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Transactional
  public Employee createEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  @Transactional
  public List<Employee> listEmployees() {
    return employeeRepository.findAll();
  }

  @Transactional
  public Optional<Employee> getEmployee(String employeeId) {
    return employeeRepository.findById(employeeId);
  }

  @Transactional
  public void deleteEmployee(String employeeId) {
    employeeRepository.deleteById(employeeId);
  }

  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
    TenantContext.setCurrentTenant("tenant1");
    employeeRepository.save(new Employee(null, "John", "Doe", null));
    TenantContext.setCurrentTenant("tenant2");
    employeeRepository.save(new Employee(null, "Jane", "Doe", null));
    TenantContext.clear();
  }

}
