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
				.route("sockjs", route -> route.path("/sockjs-node").uri(reactAppUri))
				.route("hot_update", route -> route.path("/**.hot-update.json").uri(reactAppUri))
				.route("hot_update_main", route -> route.path("/main.**").uri(reactAppUri))
				.route("sing_up", route -> route.path("/sing_up").uri(reactAppUri))
				.route("sing_in", route -> route.path("/sing_in").uri(reactAppUri))
				.route("cart", route -> route.path("/cart").uri(reactAppUri))
				.route("not_found", route -> route.path("/404").uri(reactAppUri))
				.route("search", route -> route.path("/search/{oracleId}").uri(reactAppUri))
				.route("profile", route -> route.path("/profile/**").uri(reactAppUri))
				.route("store", route -> route.path("/store/**").uri(reactAppUri))
				.route("order", route -> route.path("/order/{orderId}").uri(reactAppUri))
				.route("orders", route -> route.path("/orders").uri(reactAppUri))
				.build();
	}

}
