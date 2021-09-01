# Nearest Pharmacy Location Identifier

Return shortest distance of the pharmacy based on user provided latitude and longitude.

- The project is deployed to github at : https://github.com/lakshmanjamili/pharmacyLocationIdentifier and its access is public.
- Clone the project to local workspace.

Eclipse :

- If you are using eclipse, please import the root of the project as "existing maven project"
- right click on the project and click run as mvn install.
- right click on the main java file PharmancyLocationIndentifierApplication.java and click on Run as Java application.

I generally use visual studio :

- If you are using visual studio, import the project pointing to root folder i.e "pharmacyLocationIdentifier"
- on the terminal : mvn install : that will build the project and run all test cases etc.
- you can click run on the top and enter Run without debugging that will automatically pick the main file PharmancyLocationIndentifierApplication.java
- or you go to "Maven" section of bottom left > right click on the project > custom > spring-boot:run

For IntelliJ ide:
- After cloning project.
- File > New > Project from existing resources > select the project
- Go to main file PharmancyLocationIndentifierApplication.java > right click on the file > select Run PharmancyLocationIndentifierApplication.java file.

- the app will run on 8080 port.

- Once the app is running on 8080 port :
  
  Testing the application:
GET : /nearestpharmacylocation

as the requirement states we need to pass the latitude and longitude as request params :

/nearestpharmacylocation?latitude="Enter latitude value"&&longitude="Enter longitude value"

example:
http://localhost:8080/nearestpharmacylocation?latitude=38.852390&&longitude=-94.722740

- both lalitude and longitude are Request params that will be provided while accessing the api.

Design of project :

- The project is divided in to multiple folders controller, services, model etc
- PharmacyLocatorController.java is a @Controller that will have apis to calculate the nearest pharamcy based on input params.
- Pharmacies.csv is added to resources folder of the project.
- We have a CsvLoaderService.java is @Service class that will read "Pharmacies.csv" using openCsv dependency and put in a ArrayList.
- I've designed loading of csv to be done once we dont need to read whole csv for each api request.
- PharmacyLocatorService.java is @Service class consists of business logic to calculate the min shortest distance.
- To find out distance between origin Latitude, Longitude to Destination latitude, longitude i've used Haversine formula.

Detailed Design to identify nearest pharmacy for user:

* Get the input pharmacy details list from CSV file. (Handled in CsvLoaderService).
* For each Pharmacy item in the list calculate the distance in Miles using Harvesine formula.
* Once we get all pharmacies with total distatnce's calculated.
* Find the Pharmacy with minimum distance from list using util Comporator on distance.
* Return result back to the caller.
* The result consits of name of the pharmacy, address (total combined address : address + city + state + zip), total distance.

sample output if called the api with parameteres as below:
http://localhost:8080/nearestpharmacylocation?latitude=38.852390&&longitude=-94.722740

response:

{
     "name": "CVS PHARMACY",
     "address": "5001 WEST 135 ST LEAWOOD KS 66224",
     "totalDistance": 4.685008883444089
}

* Junit test cases are written under test folder for controller and service classes.
* ExceptionHandlerController is added to handle  unexcepted exceptions.
example : if user does not provide request params
http://localhost:8080/nearestpharmacylocation?

response :
Required parameters are missing, to calculate distance input parameters are required

Additional resources used :
https://www.geodatasource.com/distance-calculator
i've used to test and validate calculated distance between origin latitude, longitude and destination latitude, longitude.

Haversine formula : Learnt and understood about the formula
https://en.wikipedia.org/wiki/Haversine_formula

