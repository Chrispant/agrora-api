package gr.agrora_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String allowedOriginsStr;



    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("CorsConfig - allowedorigns" + allowedOriginsStr);

        if (allowedOriginsStr == null || allowedOriginsStr.isEmpty()){
            System.out.println("WARNING : CORS_ALLOWED_ORIGINS IS NULL or empty");
            return;
        }

        String[] origins = Arrays.stream(allowedOriginsStr.split(","))
                        .map(String::trim)
                        .toArray(String[]::new);

        System.out.println("CORS ORIGINS : "+Arrays.toString(origins));

        registry.addMapping("/api/**")
                .allowedOrigins(origins)
                .allowedMethods("POST", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
