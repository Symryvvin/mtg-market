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
				.route("edit_cart",
						route -> route.path("/rest/order/cart/edit")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.build();
	}

}
