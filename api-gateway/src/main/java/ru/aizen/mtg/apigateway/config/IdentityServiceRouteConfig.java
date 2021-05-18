package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityServiceRouteConfig {

	@Value("${service.identity.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator noSecureRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("registration",
						route -> route.path("/registration")
								.filters(f -> f.rewritePath("/registration", "/api/v1/registration"))
								.uri(serviceUri))
				.route("login",
						route -> route.path("/login")
								.filters(f -> f.rewritePath("/login", "/api/v1/login"))
								.uri(serviceUri))
				.build();
	}

}
