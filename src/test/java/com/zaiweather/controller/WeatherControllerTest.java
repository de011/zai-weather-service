package com.zaiweather.controller;

import com.zaiweather.model.WeatherResponse;
import com.zaiweather.service.WeatherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    private WeatherResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new WeatherResponse();
        mockResponse.setTemperatureDegrees(25);
        mockResponse.setWindSpeed(10.2);
    }

    @AfterEach
    void tearDown() {
        reset(weatherService);
    }

    @Test
    void getWeather_withDefaultCity_returnsWeatherResponse() {

        when(weatherService.getWeather()).thenReturn(mockResponse);


        WeatherResponse response = weatherController.getWeather("melbourne");


        assertNotNull(response);
        assertEquals(25, response.getTemperatureDegrees());
        assertEquals(10.2, response.getWindSpeed());
        verify(weatherService, times(1)).getWeather();
    }

    @Test
    void getWeather_withMelbourneCaseInsensitive_returnsWeatherResponse() {

        when(weatherService.getWeather()).thenReturn(mockResponse);


        WeatherResponse response = weatherController.getWeather("Melbourne");


        assertNotNull(response);
        assertEquals(25, response.getTemperatureDegrees());
        assertEquals(10.2, response.getWindSpeed());
        verify(weatherService, times(1)).getWeather();
    }

    @Test
    void getWeather_withUnsupportedCity_throwsIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> weatherController.getWeather("Sydney")
        );
        assertEquals("Only Melbourne is supported", exception.getMessage());
        verify(weatherService, never()).getWeather();
    }

    @Test
    void getWeather_whenServiceReturnsNull_throwsRuntimeException() {

        when(weatherService.getWeather()).thenReturn(null);


        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> weatherController.getWeather("melbourne")
        );
        assertEquals("Failed to fetch weather", exception.getMessage());
        verify(weatherService, times(1)).getWeather();
    }
}
