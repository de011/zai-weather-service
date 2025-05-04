package com.zaiweather.cache;

import com.zaiweather.model.WeatherResponse;
import org.springframework.stereotype.Component;

@Component
public class WeatherCache {
    private WeatherResponse cachedResponse;
    private long timestamp;

    public synchronized WeatherResponse get() {
        if (cachedResponse != null && (System.currentTimeMillis() - timestamp) < 3000) {
            return cachedResponse;
        }
        return null;
    }

    public synchronized void set(WeatherResponse response) {
        this.cachedResponse = response;
        this.timestamp = System.currentTimeMillis();
    }

    public WeatherResponse getStale() {
        return cachedResponse;
    }
}
