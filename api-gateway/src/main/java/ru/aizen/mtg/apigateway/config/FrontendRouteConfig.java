package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrontendRouteConfig {

	@Value("${service.frontend.uri}")
	private String reactAppUri;

	@Bean
	public RouteLocator frontendRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("root", route -> route.path(false,"/").uri(reactAppUri))
				.route("static", route -> route.path("/static/**").uri(reactAppUri))
				.route("sing_up", route -> route.path("/sing_up").uri(reactAppUri))
				.route("sing_in", route -> route.path("/sing_in").uri(reactAppUri))
				.route("not_found", route -> route.path("/404").uri(reactAppUri))
				.route("search", route -> route.path("/search/{oracleId}").uri(reactAppUri))
				.build();
	}

}
