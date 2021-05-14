package ru.aizen.mtg.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.application.IdentityService;
import ru.aizen.mtg.application.UserInfo;
import ru.aizen.mtg.application.UserShortInfo;
import ru.aizen.mtg.application.resource.dto.UserAddressUpdateDTO;
import ru.aizen.mtg.application.resource.dto.UserLoginDTO;
import ru.aizen.mtg.application.resource.dto.UserRegistrationDTO;
import ru.aizen.mtg.application.resource.dto.UserUpdateDTO;
import ru.aizen.mtg.application.resource.dto.response.Success;
import ru.aizen.mtg.application.resource.dto.response.UserLogin;

import java.util.Collection;

@RestController
@RequestMapping("api/v1")
public class IdentityResource {

	private final IdentityService identityService;

	@Autowired
	public IdentityResource(IdentityService identityService) {
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
				new UserLogin(true, HttpStatus.OK.value(), token, "User login successful")
		);
	}

	@PutMapping(path = "user/{username}/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> updateUser(@PathVariable("username") String username,
	                                          @RequestBody UserUpdateDTO request) {
		identityService.updateProfile(username,
				request.getFullName(),
				request.getEmail(),
				request.getPhone());
		return ResponseEntity.ok(
				new Success(HttpStatus.OK.value(), "User updated successful")
		);
	}

	@PutMapping(path = "user/{username}/address/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> updateAddress(@PathVariable("username") String username,
	                                             @RequestBody UserAddressUpdateDTO request) {
		identityService.updateProfileAddress(username,
				request.getSettlement(),
				request.getStreet(),
				request.getBuilding(),
				request.getApartment(),
				request.getPostIndex());
		return ResponseEntity.ok(
				new Success(HttpStatus.OK.value(), "User address updated successful")
		);
	}

	@GetMapping(path = "/user/{username}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfo findUser(@PathVariable("username") String username) {
		return identityService.userInfo(username);
	}

	@GetMapping(path = "/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<UserShortInfo> users() {
		return identityService.users();
	}

}
