package ru.aizen.mtg.domain.jwt;

import io.jsonwebtoken.Claims;
import ru.aizen.mtg.domain.account.Account;

import java.util.Map;

public interface TokenService {

	String generate(Account account);

	Map<String, Object> parse(String token);

}
