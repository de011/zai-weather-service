package com.zaiweather.client;

import com.zaiweather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class WeatherstackClient {

    @Value("${weatherstack.api.key}")
    private String apiKey;

    public WeatherResponse fetchWeather() {
        String url = String.format("http://api.weatherstack.com/current?access_key=%s&query=Melbourne", apiKey);
        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey("current")) return null;

        Map current = (Map) response.get("current");
        int temp = (int) current.get("temperature");
        int wind = (int) current.get("wind_speed");
        return new WeatherResponse(temp, wind);
    }
}
