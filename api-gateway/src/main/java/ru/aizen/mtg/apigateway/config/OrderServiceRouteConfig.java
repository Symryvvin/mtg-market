package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;

@Configuration
public class OrderServiceRouteConfig {

	@Value("${service.order.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator secureOrderLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("edit_cart", route -> route.path("/rest/order/cart/edit")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("add_to_cart", route -> route.path("/rest/order/cart/add")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator noSecureOrderLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("increase", route -> route.path("/rest/order/cart/{cartId}/increase/{singleId}").uri(serviceUri))
				.route("decrease", route -> route.path("/rest/order/cart/{cartId}/decrease/{singleId}").uri(serviceUri))
				.route("remove", route -> route.path("/rest/order/cart/{cartId}/remove/{singleId}").uri(serviceUri))
				.route("clear", route -> route.path("/rest/order/cart/{cartId}/clear").uri(serviceUri))
				.build();
	}

}
