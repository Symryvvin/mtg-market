package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;

@Configuration
public class StoreResourceRouteConfig {

	@Value("${service.store.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator unsecureRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("search", route -> route.path("/rest/store/singles/{oracleId}")
						.uri(serviceUri))
				.route("view_by_name", route -> route.path("/rest/store/find/{traderName}")
						.uri(serviceUri))
				.route("info", route -> route.path("/rest/store/{storeId}/info")
						.uri(serviceUri))
				.route("find", route -> route.path("/rest/store/{storeId}")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator storeRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("create", route -> route.path("/rest/store")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("edit_store", route -> route.path("/rest/store/edit")
						.filters(f -> f.filter(jwtFilter))
						.uri(serviceUri))
				.route("import_singles",
						route -> route
								.path("/rest/store/{storeId}/singles/import")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("import_singles_json",
						route -> route
								.path("/rest/store/{storeId}/singles/import/json")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("find_user_stores",
						route -> route
								.path("/rest/find/{userId}")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("add_single",
						route -> route
								.path("/rest/{storeId}/singles/add")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("edit_single_in_store",
						route -> route
								.path("/rest/{storeId}/singles/{singleId}/edit")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("delete_single_in_store",
						route -> route
								.path("/rest/{storeId}/singles/{singleId}/delete")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("reserve_singles",
						route -> route
								.path("/rest/{storeId}/singles/reserve")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("block_store",
						route -> route
								.path("/rest/{storeId}/block")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("unblock_store",
						route -> route
								.path("/rest/{storeId}/unblock")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("delete_store",
						route -> route
								.path("/rest/{storeId}/delete")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.build();
	}

}
