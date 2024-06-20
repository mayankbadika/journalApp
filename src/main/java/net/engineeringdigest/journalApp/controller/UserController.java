package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.ApplicationCache;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ApplicationCache applicationCache;

    @GetMapping
    public ResponseEntity<?> greetUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String greeting = "";

        // Call the asynchronous getWeather method
        CompletableFuture<WeatherResponse> weatherFuture = weatherService.getWeather("delhi");
        System.out.println("greeting endpoint"+Thread.currentThread().getName());

        /*
         * This code snippet asynchronously retrieves weather data and constructs a response entity based on the weather information
         * or any exceptions encountered. Let's break down the steps:
         */

        return weatherFuture.thenApply(weatherResponse -> {
            // Step 1: Applying a function to the weatherResponse
            if (weatherResponse != null) {
                // Step 2: Constructing a ResponseEntity with weather information if weatherResponse is not null
                return new ResponseEntity<>("Hi " + username + " today's weather is " + String.valueOf(weatherResponse.getCurrent().getTemp_c()) + " degree Celsius", HttpStatus.OK);
            }
            // Step 3: Constructing a ResponseEntity with just the greeting message if weatherResponse is null
            return new ResponseEntity<>("Hi " + username, HttpStatus.OK);
        }).exceptionally(ex -> {
            // Step 4: Handling exceptions that occurred during asynchronous processing
            ex.printStackTrace(); // Print the stack trace of the exception
            // Step 5: Constructing a ResponseEntity with an error message
            return new ResponseEntity<>("Hi " + username + ", an error occurred while fetching the weather data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }).join(); // Step 6: Ensure the CompletableFuture is completed before returning

    }

    /*
        The `clearAppCache` endpoint is designed to refresh the application cache without needing to restart the application.
        When the application is running, an Application Cache Bean is created,
        which initializes its `init` method and loads the configuration from the database.
        If any changes are made to the database,
        the configuration cache would typically require a restart of the application to reflect these changes.
        However, by calling this endpoint, the cache configuration collection is refreshed,
        eliminating the need for a full application restart.
     */
    @GetMapping("/clearAppCache")
    public ResponseEntity<?> clearApplicationCache() {
        applicationCache.init();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUsername(username);

        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleterUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userinDB = userService.findByUsername(username);

        userRepository.deleteByUsername(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
