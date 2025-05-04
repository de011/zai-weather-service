package com.zaiweather.service;

import com.zaiweather.client.WeatherstackClient;
import com.zaiweather.client.OpenWeatherMapClient;
import com.zaiweather.model.WeatherResponse;
import com.zaiweather.cache.WeatherCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to manage retrieval and caching of weather data
 * from multiple sources with fallback and cache support.
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    private WeatherstackClient wsClient;

    @Autowired
    private OpenWeatherMapClient owClient;

    @Autowired
    private WeatherCache cache;

    /**
     * Fetches weather data, preferring cached data when fresh,
     * and falling back to Weatherstack and then OpenWeatherMap if needed.
     *
     * @return WeatherResponse if available; otherwise, stale cache or null
     */
    public WeatherResponse getWeather() {
        WeatherResponse cached = cache.get();

        if (cached != null) {
            logger.info("Returning weather data from cache.");
            return cached;
        }

        WeatherResponse fresh = null;

        try {
            logger.info("Attempting to fetch weather data from Weatherstack.");
            fresh = wsClient.fetchWeather();
        } catch (Exception e) {
            logger.warn("Failed to fetch data from Weatherstack.", e);
        }

        if (fresh == null) {
            try {
                logger.info("Attempting to fetch weather data from OpenWeatherMap.");
                fresh = owClient.fetchWeather();
            } catch (Exception e) {
                logger.warn("Failed to fetch data from OpenWeatherMap.", e);
            }
        }

        if (fresh != null) {
            logger.info("Successfully fetched weather data, caching it.");
            cache.set(fresh);
            return fresh;
        }

        logger.warn("Both external services failed. Attempting to return stale cache.");
        return cache.getStale();
    }
}
