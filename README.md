# Taxi24 

This is a Spring boot application that provides APIs that other companies can use to manage their fleet of drivers and allocate drivers to passengers.

## Technologies
* Spring Boot
* Spring JPA  
* Spring AOP
* Maven       
* Junit5       
* MySQL        

## How to Run 

**1. Clone the application**

```bash
https://github.com/MahmoudZezo/taxi24.git
```

**2. Create Mysql database**
```bash
create database taxi24
```

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using maven**

```bash
mvn package
java -jar target/taxi24-0.0.1-SNAPSHOT.jar

```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>.

&nbsp;

## RESTful APIs ##

&nbsp;
**1. Driver APIs**

METHOD | PATH | DESCRIPTION 
------------|-----|------------
GET | /taxi24/driver | Get a list of all drivers
POST | /taxi24/driver | Create or update driver with available status
GET | /taxi24/driver//{driverId} | Get a specific driver by ID
GET | /taxi24/driver/status/{driverStatus} | Get a list of all available drivers
GET | /taxi24/driver/{specificPointLat}/{specificPointLng} | Get a list of all available drivers within 3km for a specific location

**2. Rider APIs**

METHOD | PATH | DESCRIPTION 
------------|-----|------------
GET | /taxi24/rider | Get a list of all riders
POST | /taxi24/rider | Create or update rider 
GET | /taxi24/rider/{riderId} | Get a specific rider by ID
GET | /taxi24/rider/closest/driver/{riderId} | For a specific rider, get a list of the 3 closest drivers
GET | /taxi24/rider/closest/available/driver/{riderId} | For a specific rider, get a list of the 3 closest available drivers


**3. Trip APIs**

METHOD | PATH | DESCRIPTION 
------------|-----|------------
POST | /taxi24/trip | Create a new ‘Trip’ request by assigning a driver to a rider with assigned status and make driver in unavailable status (using AOP)
GET | /taxi24/trip/start/{tripId} | Start trip and make its status Active
GET | /taxi24/trip/complete/{tripId} | Complete a trip and make its status completed, driver is available and create Invoice(using AOP). 
GET | /taxi24/trip/active | Get a list of all active Trips

**4. Invioce add pricing APIs**

METHOD | PATH | DESCRIPTION 
------------|-----|------------
POST | /taxi24/pricing | Add new or update extist price for taxi (taxi start, taix per 1 km, taxi 1 waiting hour prices)
GET | /taxi24/pricing | Get All pricing plan
GET | /taxi24/pricing/{priceId} | Get price by ID. 
GET | /taxi24/pricing/type/{priceType} | Get price by its type



**5. Invioce APIs**

METHOD | PATH | DESCRIPTION 
------------|-----|------------
GET | /taxi24/invoice | Get All invoices.
GET | /taxi24/invoice/{invoiceId} | Get invoice by ID. 
GET | /taxi24/invoice/trip/{tripId} | Get invoice by trip
