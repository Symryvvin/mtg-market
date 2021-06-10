package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;

@Configuration
public class CartResourceRouteConfig {

	@Value("${service.store.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator secureCartLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		System.out.println(serviceUri);
		return builder.routes()
				.route("edit_cart", route -> route.path("/rest/cart/edit")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("add_to_cart", route -> route.path("/rest/cart/add/{storeId}/{singleId}")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator noSecureCartLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("increase", route -> route.path("/rest/cart/{cartId}/increase/{singleId}").uri(serviceUri))
				.route("decrease", route -> route.path("/rest/cart/{cartId}/decrease/{singleId}").uri(serviceUri))
				.route("remove", route -> route.path("/rest/cart/{cartId}/remove/{singleId}").uri(serviceUri))
				.route("clear_all", route -> route.path("/rest/cart/{cartId}/clear").uri(serviceUri))
				.route("clear_store_cart", route -> route.path("/rest/cart/{cartId}/clear/{storeId}").uri(serviceUri))
				.build();
	}
}
