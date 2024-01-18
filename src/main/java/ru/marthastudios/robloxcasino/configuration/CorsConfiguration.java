package ru.marthastudios.robloxcasino.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Authorization", "Phrase-Token", "Content-Type")
                .allowedOrigins("*")
                //.allowCredentials(false)
                .exposedHeaders("Authorization", "Phrase-Token", "Content-Type")
                .allowedMethods("*");
    }
}
