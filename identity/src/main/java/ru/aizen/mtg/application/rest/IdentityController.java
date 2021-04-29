package ru.aizen.mtg.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.application.IdentityService;
import ru.aizen.mtg.application.IdentityServiceException;
import ru.aizen.mtg.application.UserInfo;
import ru.aizen.mtg.application.UserShortInfo;
import ru.aizen.mtg.application.rest.dto.UserAddressUpdateDTO;
import ru.aizen.mtg.application.rest.dto.UserLoginDTO;
import ru.aizen.mtg.application.rest.dto.UserRegistrationDTO;
import ru.aizen.mtg.application.rest.dto.UserUpdateDTO;
import ru.aizen.mtg.application.rest.response.SuccessfulResponse;
import ru.aizen.mtg.application.rest.response.UserLoginResponse;

import java.util.Collection;

@RestController
@RequestMapping("api/v1")
public class IdentityController {

	private final IdentityService identityService;

	@Autowired
	public IdentityController(IdentityService identityService) {
		this.identityService = identityService;
	}

	@PostMapping(path = "/registration",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessfulResponse> create(@RequestBody UserRegistrationDTO request) throws Exception {
		identityService.create(request.getUsername(), request.getPassword());
		return ResponseEntity.ok(
				new SuccessfulResponse(HttpStatus.OK.value(), "User registration successful")
		);
	}

	@PostMapping(path = "/login",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserLoginResponse> create(@RequestBody UserLoginDTO request) throws Exception {
		String token = identityService.authenticate(request.getUsername(), request.getPassword());
		return ResponseEntity.ok(
				new UserLoginResponse(true, HttpStatus.OK.value(), token, "User login successful")
		);
	}

	@PutMapping(path = "user/{username}/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessfulResponse> updateUser(@PathVariable("username") String username,
	                                                     @RequestBody UserUpdateDTO request)
			throws Exception {
		identityService.updateProfile(username,
				request.getFullName(),
				request.getEmail(),
				request.getPhone());
		return ResponseEntity.ok(
				new SuccessfulResponse(HttpStatus.OK.value(), "User updated successful")
		);
	}

	@PutMapping(path = "user/{username}/address/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessfulResponse> updateAddress(@PathVariable("username") String username,
	                                                        @RequestBody UserAddressUpdateDTO request)
			throws Exception {
		identityService.updateProfileAddress(username,
				request.getSettlement(),
				request.getStreet(),
				request.getBuilding(),
				request.getApartment(),
				request.getPostIndex());
		return ResponseEntity.ok(
				new SuccessfulResponse(HttpStatus.OK.value(), "User address updated successful")
		);
	}

	@GetMapping(path = "/user/{username}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfo findUser(@PathVariable("username") String username) throws IdentityServiceException {
		return identityService.userInfo(username);
	}

	@GetMapping(path = "/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<UserShortInfo> users() {
		return identityService.users();
	}

}
