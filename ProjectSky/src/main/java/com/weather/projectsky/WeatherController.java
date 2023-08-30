package com.weather.projectsky;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WeatherController {

    @Value("${openweather.api.key}")
    private String apiKey;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + apiKey + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode forecastData = objectMapper.readTree(jsonResponse);
            String cityName = forecastData.get("city").get("name").asText();
            JsonNode forecastList = forecastData.get("list");

            model.addAttribute("cityName", cityName);

            List<ForecastEntry> forecastEntries = new ArrayList<>();
            LocalDateTime currentTime = LocalDateTime.now();

            for (JsonNode forecastItem : forecastList) {
                long timestamp = forecastItem.get("dt").asLong();
                LocalDateTime forecastTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);

                if (forecastTime.isAfter(currentTime) && forecastTime.isBefore(currentTime.plusDays(3))) {
                    String date = forecastTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    double temperature = forecastItem.get("main").get("temp").asDouble();
                    String weatherDescription = forecastItem.get("weather").get(0).get("description").asText();

                    forecastEntries.add(new ForecastEntry(date, temperature, weatherDescription));
                }
            }

            model.addAttribute("forecastEntries", forecastEntries);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "weather";
    }
}
