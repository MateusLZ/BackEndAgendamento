// package br.com.api.produtos.Seguranca;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;

// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsFilter corsFilter() {
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();
//         config.addAllowedOrigin("*");
//         config.addAllowedHeader("*");
//         config.addAllowedMethod("*");
//         config.addExposedHeader("Content-Disposition");
//         config.setAllowCredentials(true);
//         source.registerCorsConfiguration("/**", config);

//         // Console para verificar configurações
//         System.out.println("Configurações CORS:");
//         System.out.println("Allowed Origins: " + config.getAllowedOrigins());
//         System.out.println("Allowed Headers: " + config.getAllowedHeaders());
//         System.out.println("Allowed Methods: " + config.getAllowedMethods());
//         System.out.println("Exposed Headers: " + config.getExposedHeaders());
//         System.out.println("Allow Credentials: " + config.getAllowCredentials());

//         return new CorsFilter(source);
//     }
// }
