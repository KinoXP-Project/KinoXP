package ek.kinoxp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // all your API endpoints
                .allowedOrigins(
                        "http://127.0.0.1:5500",
                        "http://localhost:5500",
                        "http://localhost:3000",
                        "http://localhost:5173"
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS");
//                .allowedHeaders("*")
//                .allowCredentials(false) // set true only if you actually use cookies/auth
//                .maxAge(3600);           // cache preflight for 1h
    }
}
