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
import ru.aizen.mtg.application.resource.dto.UserUpdateDTO;
import ru.aizen.mtg.application.resource.dto.response.Success;
import ru.aizen.mtg.domain.account.security.Role;

import java.util.Collection;

@RestController
@RequestMapping("/rest/user")
public class IdentityResource {

	private final IdentityService identityService;

	@Autowired
	public IdentityResource(IdentityService identityService) {
		this.identityService = identityService;
	}

	@PutMapping(path = "/edit/profile",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> updateUser(@RequestHeader("X-UserId") Long userId,
	                                          @RequestBody UserUpdateDTO request) {
		identityService.updateProfile(userId,
				request.getFullName(),
				request.getEmail(),
				request.getPhone());
		return ResponseEntity.ok(
				new Success(HttpStatus.OK.value(), "User updated successful")
		);
	}

	@PutMapping(path = "/edit/address",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> updateAddress(@RequestHeader("X-UserId") Long userId,
	                                             @RequestBody UserAddressUpdateDTO request) {
		identityService.updateProfileAddress(userId,
				request.getSettlement(),
				request.getStreet(),
				request.getBuilding(),
				request.getApartment(),
				request.getPostIndex());
		return ResponseEntity.ok(
				new Success(HttpStatus.OK.value(), "User address updated successful")
		);
	}

	@GetMapping(path = "{username}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfo userByName(@PathVariable("username") String username) {
		return identityService.userInfo(username);
	}

	@GetMapping(path = "/all",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<UserShortInfo> users() {
		return identityService.users();
	}

	@PutMapping("/become/trader")
	public ResponseEntity<Success> becomeTrader(@RequestHeader("X-UserId") Long userId) {
		identityService.changeRole(userId, Role.TRADER);

		return ResponseEntity.ok(Success.OK("Вам назначена роль 'Продавец' ваш магазин будет создан автоматически"));
	}

}
