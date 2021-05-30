package ru.aizen.mtg.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import ru.aizen.mtg.apigateway.filter.jwt.JwtFilter;
import ru.aizen.mtg.apigateway.filter.jwt.TokenData;

@Configuration
public class StoreServiceRouteConfig {

	@Value("${service.store.uri}")
	private String serviceUri;

	@Bean
	public RouteLocator unsecureRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("search", route -> route.path("/store/singles/{oracleId}")
						.uri(serviceUri))
				.route("view_user_store", route -> route.path("/store/{owner}/{name}")
						.uri(serviceUri))
				.build();
	}

	@Bean
	public RouteLocator storeRouteLocator(RouteLocatorBuilder builder, JwtFilter jwtFilter) {
		return builder.routes()
				.route("create_store",
						route -> route.path("/store/create")
								.filters(f -> f.filter(jwtFilter)
										.modifyRequestBody(String.class, CreateStore.class, MediaType.APPLICATION_JSON_VALUE,
												(exchange, body) -> {
													TokenData data = exchange.getAttribute("user.data");
													if (data != null) {
														return Mono.just(new CreateStore(
																data.getUserId(),
																data.getUsername(),
																data.getUserLocation(),
																body
														));
													} else {
														return null;
													}
												}))
								.uri(serviceUri))
				.route("edit_store",
						route -> route
								.path("/store/{storeId}/edit")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("import_singles",
						route -> route
								.path("/store/{storeId}/singles/import")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("import_singles_json",
						route -> route
								.path("/store/{storeId}/singles/import/json")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("find_user_stores",
						route -> route
								.path("/find/{userId}")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("add_single",
						route -> route
								.path("/{storeId}/singles/add")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))

				.route("edit_single_in_store",
						route -> route
								.path("/{storeId}/singles/{singleId}/edit")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("delete_single_in_store",
						route -> route
								.path("/{storeId}/singles/{singleId}/delete")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("reserve_singles",
						route -> route
								.path("/{storeId}/singles/reserve")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("block_store",
						route -> route
								.path("/{storeId}/block")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("unblock_store",
						route -> route
								.path("/{storeId}/unblock")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.route("delete_store",
						route -> route
								.path("/{storeId}/delete")
								.filters(f -> f.filter(jwtFilter))
								.uri(serviceUri))
				.build();
	}

}
