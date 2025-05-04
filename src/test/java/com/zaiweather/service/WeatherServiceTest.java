package com.zaiweather.service;

import com.zaiweather.cache.WeatherCache;
import com.zaiweather.client.OpenWeatherMapClient;
import com.zaiweather.client.WeatherstackClient;
import com.zaiweather.model.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherstackClient mockWsClient;

    @Mock
    private OpenWeatherMapClient mockOwClient;

    @Mock
    private WeatherCache mockCache;

    @InjectMocks
    private WeatherService weatherServiceUnderTest;

    @Test
    void testGetWeather_fromCache() {

        WeatherResponse cached = new WeatherResponse(25, 5.0);
        when(mockCache.get()).thenReturn(cached);


        WeatherResponse result = weatherServiceUnderTest.getWeather();

        assertEquals(cached.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(cached.getWindSpeed(), result.getWindSpeed());
    }

    @Test
    void testGetWeather_WeatherstackSucceeds() {

        when(mockCache.get()).thenReturn(null);
        WeatherResponse fromWs = new WeatherResponse(20, 3.5);
        when(mockWsClient.fetchWeather()).thenReturn(fromWs);


        WeatherResponse result = weatherServiceUnderTest.getWeather();


        assertEquals(fromWs.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(fromWs.getWindSpeed(), result.getWindSpeed());
        verify(mockCache).set(fromWs);
    }

    @Test
    void testGetWeather_OpenWeatherSucceedsWhenWeatherstackFails() {

        when(mockCache.get()).thenReturn(null);
        when(mockWsClient.fetchWeather()).thenReturn(null);
        WeatherResponse fromOw = new WeatherResponse(22, 4.0);
        when(mockOwClient.fetchWeather()).thenReturn(fromOw);


        WeatherResponse result = weatherServiceUnderTest.getWeather();


        assertEquals(fromOw.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(fromOw.getWindSpeed(), result.getWindSpeed());
        verify(mockCache).set(fromOw);
    }

    @Test
    void testGetWeather_BothApisFail_ReturnStale() {

        when(mockCache.get()).thenReturn(null);
        when(mockWsClient.fetchWeather()).thenReturn(null);
        when(mockOwClient.fetchWeather()).thenReturn(null);
        WeatherResponse stale = new WeatherResponse(18, 2.0);
        when(mockCache.getStale()).thenReturn(stale);


        WeatherResponse result = weatherServiceUnderTest.getWeather();


        assertEquals(stale.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(stale.getWindSpeed(), result.getWindSpeed());
    }
}
