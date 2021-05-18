package ru.aizen.mtg.apigateway.filter.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtFilter implements GatewayFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	private static final Pattern BEARER_PATTERN = Pattern.compile("(Bearer) ([A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*)");

	private final JwtTokenService tokenService;

	@Autowired
	public JwtFilter(JwtTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String token = getToken(exchange.getRequest().getHeaders());

		if (token == null) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		} else {
			try {
				String userId = tokenService.userId(token);
				TokenData data = tokenService.parse(token);
				exchange.getAttributes().put("user.data", data);
				exchange.mutate()
						.request(builder -> builder.header("X-UserId", userId));
				return chain.filter(exchange);
			} catch (JwtTokenServiceException e) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}
		}
	}


	private String getToken(HttpHeaders httpHeaders) {
		return httpHeaders.entrySet().stream()
				.filter(entry -> entry.getKey().equalsIgnoreCase(HttpHeaders.AUTHORIZATION))
				.flatMap(entry -> entry.getValue().stream())
				.map(value -> {
					Matcher matcher = BEARER_PATTERN.matcher(value);
					if (matcher.matches()) {
						return matcher.group(2);
					} else {
						return null;
					}
				})
				.findFirst()
				.orElse(null);
	}

}
