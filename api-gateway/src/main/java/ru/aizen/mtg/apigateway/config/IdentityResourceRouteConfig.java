package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;

@Configuration
public class IdentityResourceRouteConfig {

	@Value("${service.identity.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator noSecureIdentityRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("all", route -> route.path("/rest/user/all")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator secureIdentityRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("user_profile", route -> route.path("/rest/user/{username}")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("edit_profile", route -> route.path("/rest/user/edit/profile")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("edit_address", route -> route.path("/rest/user/edit/address")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("become_trader", route -> route.path("/rest/user/become/trader")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.build();
	}


}
