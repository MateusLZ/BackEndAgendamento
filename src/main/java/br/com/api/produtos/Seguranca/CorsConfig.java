package br.com.api.produtos.Seguranca;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean 
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); 
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); 
        config.addExposedHeader("Content-Disposition");
        config.setAllowCredentials(true); 
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
