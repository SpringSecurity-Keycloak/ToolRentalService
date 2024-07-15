[# Tool Rental App

The Tool Rental App is a point-of-sale tool for a store, like Home Depot, that rents big tools.  The below functions have been implemented: 
- Customers rent a tool for a specified number of days.
- When a customer checks out a tool, a Rental Agreement is produced.
- The store charges a daily rental fee, whose amount is different for each tool type.
- Some tools are free of charge on weekends or holidays.
- Clerks may give customers a discount that is applied to the total daily charges to reduce the final
charge.

## Documentation
The documentation and design for this service is available [here](https://github.com/SpringSecurity-Keycloak/ToolRentalService/wiki) 

## Code Generation
This server was generated by the [swagger-codegen](https://github.com/swagger-api/swagger-codegen) project.  
By using the [OpenAPI-Spec](https://github.com/swagger-api/swagger-core), you can easily generate a server stub.  
This is an example of building a swagger-enabled server in Java using the SpringBoot framework.

The underlying library integrating swagger to SpringBoot is [springdoc-openapi](https://github.com/springdoc/springdoc-openapi)

## Build Dependencies
The service is built using
- JDK 22
- Spring Boot 3
- Open API 3.0

## Build and Run

Start your server as an simple java application  using the below command
<BR>
`
mvn spring-boot:run
`
</BR>

## OpenAPI / Swagger UI 

You can view the Openapi UI documentation in swagger-ui by pointing to  
http://localhost:3000/  

Change default port value in application.properties]()