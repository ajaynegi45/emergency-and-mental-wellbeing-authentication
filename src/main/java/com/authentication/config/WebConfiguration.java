package com.authentication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)
// Do not use (debug=true) in a production system! as this contain sensitive information.
//@EnableMethodSecurity(prePostEnabled = true)
public class WebConfiguration {

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authorizeRequests) ->
                                authorizeRequests
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/api/auth/register").permitAll()
                                        .requestMatchers("/api/auth/login").permitAll()
                                        .anyRequest().authenticated()
                ).formLogin(withDefaults());

        http.oauth2Login(withDefaults());




//                .formLogin(formLogin -> formLogin.loginPage("/login"));

        return http.build();
    }
}
