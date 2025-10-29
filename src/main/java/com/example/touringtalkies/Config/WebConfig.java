package com.example.touringtalkies.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve thumbnails from your disk folder
        registry.addResourceHandler("/thumbnails/**")
                .addResourceLocations("file:/D:/Touring talkies/thumbnails/");

        // Optional: serve videos too
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:/D:/Touring talkies/videos/");
    }
}
