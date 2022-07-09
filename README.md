# customer

A service with default features:
 + Global error Handler
 + Multi language
 + Load resource from Centralize
 + Support a distributed transaction (Try-Confirm/Cancel) with API:
  * Create a customer with initial balance: POST /customers
  * Call to Payment service with OpenFeign
 + Customize OpenAPI Swagger


 http://localhost:8021/customer/v3/api-docs/
 http://localhost:8021/customer/v3/api-docs.yaml
 http://localhost:8021/customer/swagger-ui.html

 ```
 curl --location --request GET 'http://localhost:8021/customer/actuator'
 ```
