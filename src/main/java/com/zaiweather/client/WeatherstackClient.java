package com.zaiweather.client;

import com.zaiweather.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client for fetching current weather data from the Weatherstack API.
 */
@Component
public class WeatherstackClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherstackClient.class);

    @Value("${weatherstack.api.key}")
    private String apiKey;

    /**
     * Fetches current weather data for Melbourne from Weatherstack.
     *
     * @return WeatherResponse containing temperature and wind speed, or null on failure.
     */
    public WeatherResponse fetchWeather() {
        String url = String.format(
                "http://api.weatherstack.com/current?access_key=%s&query=Melbourne", apiKey
        );

        logger.info("Fetching weather data from Weatherstack API.");

        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                logger.warn("Received null response from Weatherstack API.");
                return null;
            }

            if (!response.containsKey("current")) {
                logger.warn("Missing 'current' key in Weatherstack API response: {}", response);
                return null;
            }

            Map<String, Object> current = (Map<String, Object>) response.get("current");

            int temp = (int) current.get("temperature");
            int wind = (int) current.get("wind_speed");

            logger.info("Weatherstack API response parsed: temperature={}°C, windSpeed={} km/h", temp, wind);

            return new WeatherResponse(temp, wind);

        } catch (Exception e) {
            logger.error("Error while fetching data from Weatherstack API.", e);
            return null;
        }
    }
}
