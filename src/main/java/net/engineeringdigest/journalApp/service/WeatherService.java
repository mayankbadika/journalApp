package net.engineeringdigest.journalApp.service;


import lombok.Getter;
import lombok.Setter;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.ApplicationCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;
    /*
    In Spring Boot, the @Value annotation is used to inject values from properties files into fields.
    However, you cannot use @Value directly with static fields
    because @Value works with Spring's dependency injection mechanism, which does not support static fields.
     */

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationCache applicationCache;

    @Autowired
    private RedisService redisService;

    @Async("taskExecutor")
    public CompletableFuture<WeatherResponse> getWeather(String city){
        String finalAPI = applicationCache.appCache.get("weather_api").replace("<API_KEY>", API_KEY).replace("<CITY>", city);

        try {
            System.out.println("Retriving weather data"+Thread.currentThread().getName());
            WeatherResponse weatherResponse = redisService.get(city, WeatherResponse.class);
            if(weatherResponse != null) return CompletableFuture.completedFuture(weatherResponse);

            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            if(response != null) {
                redisService.set(city, response.getBody());
            }
            return CompletableFuture.completedFuture(response.getBody());

        } catch(Exception e) {
            System.out.println("Exception occured "+e);
        }

        return CompletableFuture.completedFuture(null);
    }
}
