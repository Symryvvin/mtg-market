package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationResourceRouteConfig {

	@Value("${service.identity.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator noSecureAuthRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("registration", route -> route.path("/rest/auth/registration")
						.uri(serviceUri))
				.route("login", route -> route.path("/rest/auth/login")
						.uri(serviceUri))
				.build();
	}

}
