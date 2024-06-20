package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T get(String key, Class <T> entity) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if(o == null) return null;

            return mapper.readValue(o.toString(), entity);
        }
        catch (Exception e) {
            System.out.println("Exception occurred while retriving data from redis"+ e);
        }

        return null;
    }

    public void set(String key, Object object) {
        try {
            String jsonValue = mapper.writeValueAsString(object);

            redisTemplate.opsForValue().set(key, jsonValue);
        }
        catch (Exception e) {
            System.out.println("Exception occurred while saving data to redis");
        }
    }

}
