package com.sap.mobile.services.client.validation.broker.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	XsuaaServiceConfiguration xsuaaServiceConfiguration;

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
                jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter());
            });
        });
        return http.build();
	}

	private Converter<Jwt, ? extends AbstractAuthenticationToken> getJwtAuthenticationConverter() {
		TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
        converter.setLocalScopeAsAuthorities(true);
        return converter;
	}
	
}
