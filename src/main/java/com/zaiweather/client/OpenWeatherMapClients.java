package com.zaiweather.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaiweather.dto.MainData;
import com.zaiweather.dto.WindData;
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
public class OpenWeatherMapClients {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherMapClient.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

            Object mainRaw = response.get("main");
            Object windRaw = response.get("wind");

            if (mainRaw == null || windRaw == null) {
                logger.warn("Incomplete response: missing 'main' or 'wind' keys - {}", response);
                return null;
            }

            MainData main = objectMapper.convertValue(mainRaw, MainData.class);
            WindData wind = objectMapper.convertValue(windRaw, WindData.class);

            int temp = (int) Math.round(main.getTemp());
            double windSpeed = wind.getSpeed();

            logger.info("OpenWeatherMap API response parsed: temperature={}°C, windSpeed={} m/s", temp, windSpeed);

            return new WeatherResponse(temp, windSpeed);

        } catch (Exception e) {
            logger.error("Error while calling OpenWeatherMap API", e);
            return null;
        }
    }
}
