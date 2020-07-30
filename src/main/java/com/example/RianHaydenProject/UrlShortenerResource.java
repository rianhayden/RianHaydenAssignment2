package com.example.RianHaydenProject;
import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RequestMapping("/rest/url")
@RestController
public class UrlShortenerResource {

    //this puts values into the cache
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/{id}")
    public String getUrlMethod(@PathVariable String id) {

        String url = redisTemplate.opsForValue().get(id);
        System.out.println("URL"+ url);
        return url;
    }

    @PostMapping
    public String createUrl(@RequestBody String url){

        // This validator only allows urls starting with http or https
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

        if (urlValidator.isValid(url)){
            String id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();

            System.out.println("id:"+ id);
            redisTemplate.opsForValue().set(id, url);
            return id;
        }
        throw new RuntimeException("This url is invalid:"+ url);
    }
}
