# Spring Boot 2 Multi-Tenant Example

This application demonstrates multi-tenancy in a Spring-Boot 2 app 
using a discriminator field with Hibernate.

## Usage

```sh-session
$ createdb springbootmultitenant
$ export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/springbootmultitenant"
$ ./mvnw clean spring-boot:run
```

```sh-session
$ curl localhost:8080/api/employee -H "X-TenantID: tenant1" 
[{"userId":"16b5308b-6bb8-4a75-ae93-66dc71a0b981","firstName":"John","lastName":"Doe","tenantId":"tenant1"}]

$ curl localhost:8080/api/employee -H "X-TenantID: tenant3" 
[]

$ curl localhost:8080/api/employee -H "X-TenantID: tenant3" -H "Content-Type: application/json" -X POST -d '{"firstName":"joe"}'
{"userId":"c77ad6bb-b2ad-47f7-b21c-3e50deb6a574","firstName":"joe","lastName":null,"tenantId":"tenant3"}

$ curl localhost:8080/api/employee -H "X-TenantID: tenant3" 
[{"userId":"c77ad6bb-b2ad-47f7-b21c-3e50deb6a574","firstName":"joe","lastName":null,"tenantId":"joe"}]
```

## Detailed Description

* Uses hibernate filter to limit the query results based on tenant.
* Uses hibernate interceptors to enforce tenant details during creating/updating entities.
* Uses Spring AOP (AspectJ) to set the filter parameters.
* Each request goes thru a custom servlet filter which checks for `X-TenantID` http header and set's it in the ThreadLocal variable using `TenantContext` class. If http header is not present in request, it'll be rejected.
* Controller routes the request to Service class and the Spring AOP (`EmployeeServiceAspect` class) intercepts the service call and set's the hibernate tenant filter.
* All the service method has to be annotated with `@Transactional` for `EmployeeServiceAspect` to work.
* Above method works only for read queries, for write queries, we have to use hibernate interceptors.
* Custom Entity interceptor (using `EmptyInterceptor`) class which sets the tenantId value during the save/delete/flush-dirty entity events.
* Entity class should implement `TenantSupport` interface for the Entity interceptor to work. 

## More Info

* https://github.com/ramsrib/multi-tenant-app-demo
* https://hibernate.atlassian.net/browse/HHH-6054
* https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#multitenacy
* https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#mapping-column-filter
* https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#events

## License

MIT