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
	public RouteLocator identityResourceRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("user_profile", route -> route.path("/rest/user/**")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("my_profile", route -> route.path("/rest/user")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("all", route -> route.path("/rest/user/all")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator authorizationResourceRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("auth", route -> route.path("/rest/auth/**")
						.uri(serviceUri))
				.build();
	}


}
