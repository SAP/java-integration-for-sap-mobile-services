package com.sap.mobile.services.client.validation.broker.configuration;

import com.sap.cloud.security.spring.config.IdentityServicesPropertySourceFactory;
import com.sap.cloud.security.spring.config.XsuaaServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@PropertySource(factory = IdentityServicesPropertySourceFactory.class, ignoreResourceNotFound = true, value = { "" })
public class SecurityConfiguration {

	@Autowired
	XsuaaServiceConfiguration xsuaaServiceConfiguration;
	
	@Autowired
	Converter<Jwt, AbstractAuthenticationToken> authConverter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf->csrf.disable()).
		sessionManagement((sessionManagement) -> {
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }).authorizeHttpRequests((authorizeRequests) -> {
            authorizeRequests.requestMatchers(HttpMethod.GET, "/v3/api-docs").permitAll()
                    .anyRequest().authenticated();
        }).oauth2ResourceServer((oauth2ResourceServer) -> {
            oauth2ResourceServer.jwt(jwt -> {
                jwt.jwtAuthenticationConverter(authConverter);
            });
        });
        return http.build();
	}
	
}
