package com.zaiweather.service;

import com.zaiweather.client.WeatherstackClient;
import com.zaiweather.client.OpenWeatherMapClient;
import com.zaiweather.model.WeatherResponse;
import com.zaiweather.cache.WeatherCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Autowired
    private WeatherstackClient wsClient;
    @Autowired
    private OpenWeatherMapClient owClient;
    @Autowired
    private WeatherCache cache;

    public WeatherResponse getWeather() {
        WeatherResponse cached = cache.get();
        if (cached != null) return cached;

        WeatherResponse fresh = null;
        try {
            fresh = wsClient.fetchWeather();
        } catch (Exception ignored) {}

        if (fresh == null) {
            try {
                fresh = owClient.fetchWeather();
            } catch (Exception ignored) {}
        }

        if (fresh != null) {
            cache.set(fresh);
            return fresh;
        }

        return cache.getStale();
    }
}
