package casoft.mvc.util.filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*"); // Ou especifique seu domínio, ex: "http://localhost:3000"
        config.setAllowCredentials(true);

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");      // Adicione PUT aqui
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("OPTIONS"); // OPTIONS é usado no preflight, importante liberar

        config.addAllowedHeader("*");        // Libera todos os headers, incluindo Authorization
        config.addExposedHeader("Authorization"); // Opcional, se quiser expor

        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<AccessFilter> registrationBean(){
        FilterRegistrationBean<AccessFilter> register = new FilterRegistrationBean<>();
        register.setFilter(new AccessFilter());
        register.addUrlPatterns("/api/admin/*");
        register.setOrder(1);
        return register;
    }
}
