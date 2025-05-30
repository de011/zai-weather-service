package com.zaiweather.client;

import com.zaiweather.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client for retrieving current weather data from the OpenWeatherMap API.
 */
@Component
public class OpenWeatherMapClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherMapClient.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;

    /**
     * Fetches current weather data for Melbourne from OpenWeatherMap.
     *
     * @return WeatherResponse containing temperature and wind speed, or null on failure.
     */
    public WeatherResponse fetchWeather() {
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/weather?q=melbourne,AU&appid=%s&units=metric",
                apiKey
        );

        logger.info("Fetching weather data from OpenWeatherMap API.");

        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null) {
                logger.warn("Received null response from OpenWeatherMap API.");
                return null;
            }

            if (!response.containsKey("main") || !response.containsKey("wind")) {
                logger.warn("Incomplete response: missing 'main' or 'wind' keys - {}", response);
                return null;
            }

            Map<String, Object> main = (Map<String, Object>) response.get("main");
            Map<String, Object> wind = (Map<String, Object>) response.get("wind");

            int temp = (int) Math.round((Double) main.get("temp"));
            double windSpeed = ((Number) wind.get("speed")).doubleValue();

            logger.info("OpenWeatherMap API response parsed: temperature={}°C, windSpeed={} m/s", temp, windSpeed);

            return new WeatherResponse(temp, windSpeed);

        } catch (Exception e) {
            logger.error("Error while calling OpenWeatherMap API", e);
            return null;
        }
    }
}
