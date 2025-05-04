package com.zaiweather.cache;

import com.zaiweather.model.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * In-memory cache for weather data with TTL configurable via properties.
 */
@Component
public class WeatherCache {

    private static final Logger logger = LoggerFactory.getLogger(WeatherCache.class);

    private WeatherResponse cachedResponse;
    private long timestamp;

    @Value("${weather.cache.ttl:3000}")
    private long cacheTtlMillis;

    /**
     * Returns the cached weather response if it's still within the configured TTL.
     *
     * @return Fresh WeatherResponse or null
     */
    public synchronized WeatherResponse get() {
        long now = System.currentTimeMillis();
        if (cachedResponse != null && (now - timestamp) < cacheTtlMillis) {
            logger.info("Cache hit: returning cached weather response.");
            return cachedResponse;
        }
        logger.info("Cache miss: no valid cached weather response found.");
        return null;
    }

    /**
     * Caches a new weather response.
     *
     * @param response New data to cache
     */
    public synchronized void set(WeatherResponse response) {
        this.cachedResponse = response;
        this.timestamp = System.currentTimeMillis();
        logger.info("Weather response cached at timestamp: {}", timestamp);
    }

    /**
     * Returns the last cached response even if it might be stale.
     *
     * @return Possibly outdated WeatherResponse
     */
    public WeatherResponse getStale() {
        logger.info("Returning stale weather response (might be outdated).");
        return cachedResponse;
    }
}
