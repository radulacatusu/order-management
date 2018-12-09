# Spring Boot REST API for Order management

This is a RESTFUL web service in Java using Spring Boot (RestController)
* Create new product
* Retrieve a list of all products
* Update a product
* Placing an order
* Retrieving all orders within a given time period
* (Re)calculate the total order amount

## About

The application is developed with Spring Boot 2.1.0.RELEASE and maven as a building tool.
The database used is postgres.

## To run the application
Use one of the several ways of running a Spring Boot application. Below are just three options:

1. Build using maven goal: `mvn clean package` and execute the resulting artifact as follows `java -jar order-manangement-1.0-SNAPSHOT.jar` or
2. On Unix/Linux based systems: run `mvn clean package` then run the resulting jar as any other executable `./order-manangement-1.0-SNAPSHOT.jar`

## API Documentation

Swagger can be accessed at http://localhost:8090/swagger-ui.html or http://34.209.73.115:8090/swagger-ui.html

## Testing the API

1. Integration tests with @SpringBootTest over a in memory database
2. Mocking with @MockBean for the service layer
3. Integration Testing with @DataJpaTest for the persistence layer

## To manually test the application

I used POSTMAN and cURL to test the application. 
It is also deployed on AWS - http://34.209.73.115:8090 and connected to a Postgres AWS instance


### Create Product
```
POST - http://localhost:8090/products or http://34.209.73.115:8090/products
```
##### Body
```
{
	"name": "Test product",
	"price": 60.00
}
```
Response: 201 CREATED
```
{
    "id": "1"
}
```
### Update Order
```
PUT - http://localhost:8090/products/1 or http://34.209.73.115:8090/products/1
```
##### Body
```
{
	"price": 80.00
}
```
Response: 200 OK
```
{
    "id": 1,
    "name": "Test product",
    "price": "80.00"
}
```
### Get all products
```
GET - http://localhost:8090/products or http://34.209.73.115:8090/products
```
Response: 200 OK
```
[
    {
        "id": 1,
        "name": "Test product",
        "price": "80.00"
    },
    {
        "id": 2,
        "name": "Test product two",
        "price": "60.00"
    }
]
```
### Place an order
```
POST - http://localhost:8090/orders or http://34.209.73.115:8090/orders
```
##### Body
```
{
	"email": "test@test.com",
	"products": [1, 2]
}
```
Response: 201 CREATED
```
{
    "orderId": 1,
    "orderAmount": 140
}
```
### Retrieving all orders within a given time period
```
GET - http://localhost:8090/orders?start=2018-12-08 16:00:00&end=2018-12-08 17:00:00 or
      http://34.209.73.115:8090/orders?start=2018-12-08 16:00:00&end=2018-12-08 17:00:00
```
Response: 200 OK
```
[
    {
        "id": 1,
        "email": "test@test.com",
        "orderDate": "2018-12-08T14:50:25.662+0000",
        "orderAmount": "140.00",
        "products": [
            {
                "id": 1,
                "name": "Test product",
                "price": "80.00"
            },
            {
                "id": 2,
                "name": "Test product two",
                "price": "60.00"
            }
        ]
    }
]
```
### (Re)calculate the total order amount after the price of a product was updated
```
PUT - http://localhost:8090/orders/1/calculate or http://34.209.73.115:8090/orders/1/calculate
```
##### Body
```
{
    "id": 1,
    "email": "test@test.com",
    "orderDate": "2018-12-08T14:50:25.662+0000",
    "orderAmount": "170.00",
    "products": [
        {
            "id": 2,
            "name": "Test product two",
            "price": "60.00"
        },
        {
            "id": 1,
            "name": "Test product",
            "price": "110.00"
        }
    ]
}
```
Response: 200 OK

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Radu Stefan Lacatusu** - [radulacatusu](https://github.com/radulacatusu/)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details