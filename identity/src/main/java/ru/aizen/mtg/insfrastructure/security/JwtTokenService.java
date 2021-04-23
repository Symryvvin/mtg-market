package ru.aizen.mtg.insfrastructure.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.aizen.mtg.domain.account.Account;
import ru.aizen.mtg.domain.jwt.TokenService;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenService implements TokenService {

	@Value("${jwt.privateKey}")
	private String privateKeyValue;
	@Value("${jwt.publicKey}")
	private String publicKeyValue;

	private PrivateKey privateKey;
	private PublicKey publicKey;

	@PostConstruct
	public void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
		privateKey = PemKeyUtils.privateKeyFromString(privateKeyValue);
		publicKey = PemKeyUtils.publicKeyFromString(publicKeyValue);
	}

	@Override
	public String generate(Account account) {
		Date date = Date.from(LocalDateTime.now().plusHours(12).atZone(ZoneId.systemDefault()).toInstant());
		return Jwts.builder()
				.setSubject(account.getLogin())
				.setClaims(
						Map.of("role", account.getRole(),
								"id", account.getId(),
								Claims.SUBJECT, account.getLogin(),
								"is_blocked", account.isBlocked()))
				.setExpiration(date)
				.setHeaderParam("alg", "RS256")
				.signWith(privateKey, SignatureAlgorithm.RS256)
				.compact();

	}

	@Override
	public Map<String, Object> parse(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

}
