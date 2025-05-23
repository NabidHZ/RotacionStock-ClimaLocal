package com.inditex.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;



@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public String fetchRawForecast(String lat, String lon) {
        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&daily=temperature_2m_max,precipitation_sum&timezone=Europe/Madrid",
                lat, lon);
        return restTemplate.getForObject(url, String.class);
    }

    public double obtenerTemperaturaMaxima(String lat, String lon) {
        String rawJson = fetchRawForecast(lat, lon);
        JSONObject json = new JSONObject(rawJson);
        JSONArray temps = json.getJSONObject("daily").getJSONArray("temperature_2m_max");
        return temps.getDouble(0); // Primer día
    }

    // En WeatherService.java
    public boolean hayPrevisionLluvia(String lat, String lon) {
        String rawJson = fetchRawForecast(lat, lon);
        JSONObject json = new JSONObject(rawJson);
        JSONArray rain = json.getJSONObject("daily").getJSONArray("precipitation_sum");
        return rain.getDouble(0) > 0; // Si hay precipitación el primer día
    }
}