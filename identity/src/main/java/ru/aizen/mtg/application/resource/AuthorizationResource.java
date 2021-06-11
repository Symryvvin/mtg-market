package ru.aizen.mtg.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aizen.mtg.application.IdentityService;
import ru.aizen.mtg.application.resource.dto.UserLoginDTO;
import ru.aizen.mtg.application.resource.dto.UserRegistrationDTO;
import ru.aizen.mtg.application.resource.dto.response.UserLogin;

@RestController
@RequestMapping("/rest/auth")
public class AuthorizationResource {

	private final IdentityService identityService;

	@Autowired
	public AuthorizationResource(IdentityService identityService) {
		this.identityService = identityService;
	}

	@PostMapping(path = "/registration",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> create(@RequestBody UserRegistrationDTO request) {
		identityService.create(request.getUsername(), request.getPassword());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping(path = "/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserLogin> create(@RequestBody UserLoginDTO request) {
		String token = identityService.authenticate(request.getUsername(), request.getPassword());
		return ResponseEntity.ok(
				new UserLogin(true, HttpStatus.OK.value(), token, "Вход выполнен успешно")
		);
	}

}
