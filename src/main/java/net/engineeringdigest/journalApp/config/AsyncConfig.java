package net.engineeringdigest.journalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/*
 * In a typical Spring Boot application:
 *
 * Worker Threads:
 * HTTP requests to controller-based endpoints are handled by worker threads. When a client sends an HTTP request
 * to a specific endpoint, the servlet container (such as Tomcat, Jetty, or Undertow) assigns a worker thread from
 * its thread pool to handle that request. This worker thread then executes the corresponding controller method to
 * process the request and generate a response. Each worker thread in the servlet container's thread pool is
 * responsible for handling a single HTTP request at a time. Once the request processing is complete, the worker
 * thread is returned to the pool to be reused for handling subsequent requests. This approach allows the application
 * to handle multiple concurrent requests efficiently without blocking the main thread or other worker threads. It
 * ensures that the application remains responsive and can serve multiple clients simultaneously.
 *
 * Main Thread:
 * The main thread in a Spring Boot application is responsible for initializing the application context, setting up
 * various components, and starting the embedded web server. It initializes the Spring application context by scanning
 * for components (beans) annotated with @Component, @Service, @Repository, @Controller, etc., and performs dependency
 * injection to wire together the dependencies between components. Additionally, it reads application properties and
 * configuration files, sets up the environment for the application to run, and handles lifecycle events of the application
 * context, such as initialization and destruction. Once the initialization is complete and the embedded web server is
 * started, the main thread typically transitions into a waiting state, allowing the application to handle incoming
 * requests. The main thread may also be responsible for monitoring and managing the application, such as registering
 * with monitoring tools and performing health checks.
 */


@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name ="taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("userThread-");
        executor.initialize();
        return executor;
    }
}
