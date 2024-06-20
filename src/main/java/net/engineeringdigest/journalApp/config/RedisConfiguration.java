package net.engineeringdigest.journalApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * The AppConfig class is annotated with @Configuration to indicate that it is a source
 * of bean definitions for the Spring IoC container. The @Configuration annotation serves
 * several important roles in a Spring Boot application:
 *
 * 1. Defining Bean Methods: @Configuration classes declare one or more @Bean methods
 *    that instantiate, configure, and initialize objects managed by the Spring container.
 *
 * 2. Equivalent to XML Configuration: It provides a Java-based alternative to XML
 *    configuration, making the configuration type-safe and easier to refactor.
 *
 * 3. Enhances Readability and Maintainability: Java-based configuration is more readable
 *    and maintainable compared to XML, leveraging IDE capabilities such as code
 *    completion and refactoring tools.
 *
 * 4. Component Scanning and Conditional Configuration: Works with component scanning
 *    (@ComponentScan) and conditional configuration (@Conditional), allowing for more
 *    flexible and dynamic bean definitions.
 *
 * 5. Supports Externalized Configuration: Enables easy externalization of configuration
 *    using @Value and properties files, which is particularly useful in Spring Boot.
 *
 * 6. Enables Advanced Configuration Options: Supports advanced configuration options
 *    such as profiles (@Profile), conditional bean loading (@Conditional), and enabling
 *    specific configurations (@Enable* annotations).
 *
 * Overall, the @Configuration annotation is fundamental in Spring Boot for setting up the
 * application context, replacing XML configuration with a more manageable Java-based
 * approach, enhancing readability, maintainability, and flexibility.
 */

@Configuration
public class RedisConfiguration {
    /**
     * The RedisConfiguration class is annotated with @Configuration to indicate that it is a
     * source of bean definitions for the Spring IoC container. Here’s an explanation of how
     * the bean initialization and dependency injection work in this context:
     *
     * 1. Bean Definition: Beans are defined using the @Bean annotation within the
     *    @Configuration class. In this case, we have a bean for redisTemplate.
     *
     * 2. RedisTemplate Bean: The redisTemplate(RedisConnectionFactory factory) method defines
     *    a bean of type RedisTemplate<String, Object>. This method takes a RedisConnectionFactory
     *    as a parameter.
     *
     * 3. Dependency Injection: When Spring initializes the application context, it will
     *    automatically inject the RedisConnectionFactory bean into the redisTemplate method.
     *    Spring does this by looking for existing beans of the required type (RedisConnectionFactory)
     *    in the application context.
     *
     * 4. Initialization Order: Spring will first create and initialize the RedisConnectionFactory
     *    bean because it is a dependency for the redisTemplate bean. Once the RedisConnectionFactory
     *    is available, Spring will pass it to the redisTemplate method when initializing the
     *    RedisTemplate bean.
     *
     * 5. Serializers: Optional configurations for serializers are provided to customize how
     *    keys and values are serialized and deserialized. StringRedisSerializer is used for keys,
     *    and GenericJackson2JsonRedisSerializer for values, allowing for JSON serialization.
     */
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();

        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
