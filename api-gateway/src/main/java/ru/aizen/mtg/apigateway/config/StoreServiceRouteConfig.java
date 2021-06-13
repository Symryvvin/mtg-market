package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;

@Configuration
public class StoreServiceRouteConfig {

	@Value("${service.store.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator storeResourceRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("edit", route -> route.path("/rest/store/edit")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("block", route -> route.path("/rest/store/block")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("unblock", route -> route.path("/rest/store/unblock")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("create", route -> route.path("/rest/store")
						.uri(serviceUri))
				.route("view_store", route -> route.path("/rest/store/view/{traderName}")
						.uri(serviceUri))
				.route("store_info", route -> route.path("/rest/store/trader/{traderId}")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator singleResourceRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("create", route -> route.path("/rest/single/add")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("import", route -> route.path("/rest/single/import/**")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("edit_singles", route -> route.path("/rest/single/edit/{singleId}")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("delete_json", route -> route.path("/rest/single/delete/{singleId}")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("search", route -> route.path("/rest/single/search/{oracleId}")
						.uri(serviceUri))
				.route("reserve", route -> route.path("/rest/single/reserve/{singleId}")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator cartRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("cart", route -> route.path("/rest/cart/**")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.build();
	}


}
