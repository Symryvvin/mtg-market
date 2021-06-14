package ru.aizen.mtg.insfrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aizen.mtg.domain.account.Account;
import ru.aizen.mtg.domain.account.AccountException;
import ru.aizen.mtg.domain.jwt.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JwtTokenServiceTest {

	@Autowired
	TokenService tokenService;

	static {
		System.setProperty("JWT_PRIVATE_KEY", "-----BEGIN PRIVATE KEY-----\n" +
				"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDPqdJDSaR4XcpM\n" +
				"e91dWZiTY/G3So0+lxN1FZh8+6FOGisV01RbaDNbJ8WWjT9L20JJWF3vUzd003bb\n" +
				"RL3hA36RyioXZXkUOU5akCxVvpGOnnKh7I0H3hVfAx6B2ZEDZ4uEbz2dyAe7u+w+\n" +
				"PGgCBx4tFs4dhjKlJQp1iu4I5sEoOpLQk7y/OqDzRuebbGYaTmAQqNKFrYNfHeHI\n" +
				"AxJ+c4bUYT+vxbfnjvaSrVfTjObaK6gIJLCJ+JKLkYXguvWx4uJumtqL25y2SEQ7\n" +
				"ZMl4eiXWwSI9ctIO7lc4gCHNSscuPHe3Ol5h0tLIbCGWrQM6W7LqEMCX8lH8bMSA\n" +
				"S40FZNnbAgMBAAECggEARdscriHxv8PdL4ljV+mx17MBNuVPSkTL+NEA8No80uce\n" +
				"o0Lp163fyMRCRXjSWBQz4jRltQJLX3NbQjuHnE/uneBh9GjgKBt27iQdpeJrWIyY\n" +
				"hzvp3UGp8sI72vZzP6/vUOeS1KbbYx2+RNlRK411azmGsIvaMY+aSxwnIrttMriV\n" +
				"sMgMwATHq9rC/0TTIz8SVQZsPsdgGVQ56p1ZKmwIhS8ttmk6Jz+tNaH7jjB9fkRQ\n" +
				"rS6QdIjyFy+Mf0WY5j6k4sYcnqF9m5FIzpryZ+kFtoG24K54jU8SHMV4MpQ+PWDl\n" +
				"WX3IBhcgGni4h21wCvDELqwNGFSvUyQXh90IwR8rkQKBgQD8pHfPYwZHybpMbPKD\n" +
				"IzCC/HiiHQi8c4Sz0IR3H9mFjLw3ht47ilr8GDYONgLRUhLOv++m45bL4Pbey5sS\n" +
				"l8pgpFe5cv3qVHuIaBqplE16zVDtWC5rv9rqL58QXjp8Pfxic/D2imCwARfA1l7p\n" +
				"UvU1gPDH5cyC9GVqxCsRNDNQ/wKBgQDSbFOxoHc8zFh7M5ZmsSPKZ89IenASOmTp\n" +
				"JNA+DrhBUWb9XYTwaOMDf1RGYp6AeFpyMiE7UEwB+rtPPTtKJj9NK77l3ev6FgJI\n" +
				"xiu2X7vThaXWn8PLCN+kitLAMfSrcHUwMsjRrPSbYuwFJ0IZtraNxFjB2/Qo0WmI\n" +
				"1YEcvFDbJQKBgQCN4L731CB+yCa7LKO0SVrA9I+6WpvBWQ44/OynCVfazAIXHMJI\n" +
				"pZYa5G3W5TYH3xN40AkBq+XPcPKyg5z+gAfC5gftuUonAnKtU+lFOmeYgxzjbRYE\n" +
				"pKWHxl6JjqFBjH3jvECh82wA61wvizOZzGCwP0z4tXB4Sk95+dhFvQ7keQKBgAoS\n" +
				"d1scRDjbyyxyhrN5rS3mFCzy92RX+TaTtYgS6kzuGZDtJgzf6GXldWI9HDsoB6RJ\n" +
				"s/+D02+Q0eB0HN9qv0LxYhzcp4v7+7YCDluXb4Urc4m3cgbI8POOVZIvkfeW1XUu\n" +
				"KSCoN7ksXgHNJEPY1BorGNiGr8Q4aeXL2ndGqFQlAoGBAILSaafJ1jrYvIUJosWV\n" +
				"oQjr0oIV7iXmYRJAB4B4XvoDUjNQBF6/tIhg4TrIyVzQN+w9h7J5taTNzcnWm89L\n" +
				"OsgIjdMVE3L4c3v7eNkdVTqnxvo+fEKEggd6gpMq7Xor1f09nY/IQZo4LTz/XyEl\n" +
				"hJMlh/+Gls16oxTQoHldL3w6\n" +
				"-----END PRIVATE KEY-----");
		System.setProperty("JWT_PUBLIC_KEY", "-----BEGIN PUBLIC KEY-----\n" +
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz6nSQ0mkeF3KTHvdXVmY\n" +
				"k2Pxt0qNPpcTdRWYfPuhThorFdNUW2gzWyfFlo0/S9tCSVhd71M3dNN220S94QN+\n" +
				"kcoqF2V5FDlOWpAsVb6Rjp5yoeyNB94VXwMegdmRA2eLhG89ncgHu7vsPjxoAgce\n" +
				"LRbOHYYypSUKdYruCObBKDqS0JO8vzqg80bnm2xmGk5gEKjSha2DXx3hyAMSfnOG\n" +
				"1GE/r8W35472kq1X04zm2iuoCCSwifiSi5GF4Lr1seLibprai9uctkhEO2TJeHol\n" +
				"1sEiPXLSDu5XOIAhzUrHLjx3tzpeYdLSyGwhlq0DOluy6hDAl/JR/GzEgEuNBWTZ\n" +
				"2wIDAQAB\n" +
				"-----END PUBLIC KEY-----\n");
	}

	@Test
	void generate() throws AccountException {
		String token = tokenService.generate(Account.register("login", "password"));
		assertEquals("login", tokenService.parse(token).get("username"));
	}

}