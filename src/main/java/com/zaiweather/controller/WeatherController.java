package com.zaiweather.controller;

import com.zaiweather.model.WeatherResponse;
import com.zaiweather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    @Autowired
    private WeatherService service;

    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam(defaultValue = "melbourne") String city) {
        if (!city.equalsIgnoreCase("melbourne")) {
            throw new IllegalArgumentException("Only Melbourne is supported");
        }
        WeatherResponse response = service.getWeather();
        if (response == null) {
            throw new RuntimeException("Failed to fetch weather");
        }
        return response;
    }
}
