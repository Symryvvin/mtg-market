package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;

@Configuration
public class IdentityServiceRouteConfig {

	@Value("${service.identity.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator noSecureRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("registration", route -> route.path("/registration")
						.uri(serviceUri))
				.route("login", route -> route.path("/login")
						.uri(serviceUri))
				.route("username", route -> route.path("/user/{userId}/username")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator secureRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("registration",
						route -> route.path("/user/{username}/address/edit")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.build();
	}

}
