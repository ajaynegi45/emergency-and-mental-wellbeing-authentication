package com.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity(debug = true)
// Do not use (debug=true) in a production system! as this contain sensitive information.
//@EnableMethodSecurity(prePostEnabled = true)
public class WebConfiguration {
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authorizeRequests) ->
                                authorizeRequests
                                        .requestMatchers("/api/auth/register").permitAll()
                                        .requestMatchers("/api/auth/login").permitAll()
                                        .anyRequest().authenticated()
                )
                .formLogin(formLogin->formLogin.loginPage("/login").permitAll())
                .oauth2Login(oauthLogin->oauthLogin.loginPage("/login").permitAll());
  
       

//                .formLogin(formLogin -> formLogin.loginPage("/login"));
        return http.build();
    }
}
