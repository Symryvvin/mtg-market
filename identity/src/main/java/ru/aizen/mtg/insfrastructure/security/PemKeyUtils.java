package ru.aizen.mtg.insfrastructure.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PemKeyUtils {

	public static PrivateKey privateKeyFromString(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		privateKey = privateKey.replaceAll("\\n", "")
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll(" ", "");
		return KeyFactory.getInstance("RSA")
				.generatePrivate(
						new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
				);
	}

	public static PublicKey publicKeyFromString(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		publicKey = publicKey.replaceAll("\\n", "")
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll(" ", "");
		return KeyFactory.getInstance("RSA")
				.generatePublic(
						new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
				);
	}

}