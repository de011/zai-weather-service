# zai-weather-service

 Zai Weather Service

This project is a weather microservice for Melbourne that:

    Uses Weatherstack as the primary weather provider

    Uses OpenWeatherMap as a failover provider

    Returns a unified JSON response: temperature in Celsius and wind speed

    Implements a 3-second cache and serves stale data if all providers are down

    Designed for resilience, modularity, and developer extensibility

 Tech Stack

    Java 21

    Spring Boot 3

    Maven

    REST APIs via Spring MVC

    In-memory cache

    RestTemplate for external API calls

    SLF4J Logging


 Setup Instructions
1. Clone the Project

git clone https://github.com/your-org/zai-weather-service.git
cd zai-weather-service

2. Add Your API Keys

Create src/main/resources/application.properties with:

server.port=8080
weatherstack.api.key=YOUR_WEATHERSTACK_KEY
openweathermap.api.key=YOUR_OPENWEATHERMAP_KEY

     Replace with real API keys from:

        https://weatherstack.com

        https://openweathermap.org

3. Build the Project

./mvnw clean package

4. Run the Service

java -jar target/zai-weather-service-0.0.1-SNAPSHOT.jar

 API Usage
 Get Melbourne Weather

curl "http://localhost:8080/v1/weather?city=melbourne"

Example Response:

{
"temperatureDegrees": 27,
"windSpeed": 18.3
}

 Unsupported City

curl "http://localhost:8080/v1/weather?city=sydney"

Example Response:

{
"error": "Only Melbourne is supported"
}


💡 Design Decisions & Trade-offs
Feature	                                Implementation
Failover	              Tries Weatherstack first, OpenWeatherMap second
Caching	                  3-second TTL in-memory cache with fallback to stale
Logging                   Standard SLF4J logging integrated via Spring Boot
Extensibility	          Service and client classes are decoupled for easy additions
Error Resilience	      Graceful fallback; logs root cause
Testing                   Unit test stubs can be added via spring-boot-starter-test