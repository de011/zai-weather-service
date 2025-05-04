package com.zaiweather.client;

import com.zaiweather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class OpenWeatherMapClient {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public WeatherResponse fetchWeather() {
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/weather?q=melbourne,AU&appid=%s&units=metric", apiKey);
        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey("main") || !response.containsKey("wind")) return null;

        Map main = (Map) response.get("main");
        Map wind = (Map) response.get("wind");

        int temp = (int) Math.round((Double) main.get("temp"));
        double windSpeed = ((Number) wind.get("speed")).doubleValue();
        return new WeatherResponse(temp, windSpeed);
    }
}
