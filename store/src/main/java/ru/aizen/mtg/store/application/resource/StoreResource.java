package ru.aizen.mtg.store.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aizen.mtg.store.application.resource.dto.request.CreateStoreDTO;
import ru.aizen.mtg.store.application.resource.dto.response.StoreInfoRepresentation;
import ru.aizen.mtg.store.application.resource.dto.response.StorePresentation;
import ru.aizen.mtg.store.application.service.StoreService;

@RestController
@RequestMapping("/rest/store")
public class StoreResource {

	private final StoreService storeService;

	@Autowired
	public StoreResource(StoreService storeService) {
		this.storeService = storeService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> create(@RequestBody CreateStoreDTO request) {
		storeService.create(request.getUserId(), request.getUsername(), request.getUserLocation());
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	public StorePresentation edit(@RequestHeader("X-UserId") Long userId) {
		return StorePresentation.of(storeService.store(userId));
	}

	@GetMapping(path = "/view/{traderName}", produces = MediaType.APPLICATION_JSON_VALUE)
	public StorePresentation view(@PathVariable("traderName") String trader) {
		return StorePresentation.view(storeService.store(trader));
	}

	@GetMapping(path = "/trader/{traderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public StoreInfoRepresentation info(@PathVariable("traderId") long traderId) {
		return StoreInfoRepresentation.from(storeService.store(traderId));
	}

	@PostMapping(path = "/block",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> blockStore(@RequestHeader("X-UserId") Long userId) {
		storeService.blockStore(storeService.store(userId));
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/unblock",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> unblockStore(@RequestHeader("X-UserId") Long userId) {
		storeService.unblockStore(storeService.store(userId));
		return ResponseEntity.ok().build();
	}

}
