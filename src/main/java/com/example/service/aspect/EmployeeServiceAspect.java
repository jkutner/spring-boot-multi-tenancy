package com.example.service.aspect;

import com.example.service.EmployeeService;
import com.example.util.TenantContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmployeeServiceAspect {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  // only applicable to employee service
  @Before("execution(* com.example.service.EmployeeService.*(..)) && !execution(* com.example.service.EmployeeService.run(..)) && target(employeeService)")
  public void aroundExecution(JoinPoint pjp, EmployeeService employeeService) throws Throwable {
    org.hibernate.Filter filter = employeeService.entityManager.unwrap(Session.class).enableFilter("tenantFilter");
    filter.setParameter("tenantId", TenantContext.getCurrentTenant());
    filter.validate();
  }
}
