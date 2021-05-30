package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchServiceRouteConfig {

	@Value("${service.search.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator noSecureSearchLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("random",
						route -> route.path("/rest/search/random")
								.uri(serviceUri))
				.route("search",
						route -> route.path("/rest/search/auto")
								.uri(serviceUri))
				.build();
	}

}
