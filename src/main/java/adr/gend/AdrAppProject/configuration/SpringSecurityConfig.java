package adr.gend.AdrAppProject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SpringSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http   .csrf().disable()
                        .authorizeHttpRequests(authorize->{
                            authorize.requestMatchers("/page/upload*").hasRole("ADMIN");
                            authorize.requestMatchers("/page/admin-area").hasRole("ADMIN");
                            authorize.requestMatchers("/page/delete*").hasRole("ADMIN");
                            authorize.anyRequest().permitAll();
                        }).httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
