package com.zaiweather.controller;

import com.zaiweather.model.WeatherResponse;
import com.zaiweather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that exposes endpoints for retrieving weather data.
 */
@RestController
@RequestMapping("/v1")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService service;

    /**
     * Retrieves current weather information for the specified city.
     * Currently, only the city "Melbourne" is supported.
     *
     * @param city Name of the city (default is melbourne)
     * @return WeatherResponse object containing temperature and wind speed
     * @throws IllegalArgumentException if any city other than Melbourne is requested
     * @throws RuntimeException if weather data could not be fetched
     */

    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam(defaultValue = "melbourne") String city) {
        logger.info("Received request for weather data for city: {}", city);

        if (!city.equalsIgnoreCase("melbourne")) {
            logger.warn("Unsupported city requested: {}", city);
            throw new IllegalArgumentException("Only Melbourne is supported");
        }

        WeatherResponse response = service.getWeather();

        if (response == null) {
            logger.error("Failed to fetch weather data for Melbourne.");
            throw new RuntimeException("Failed to fetch weather");
        }

        logger.info("Successfully fetched weather data: {}", response);
        return response;
    }
}
