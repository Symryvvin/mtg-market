package ru.aizen.mtg.store.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.aizen.mtg.store.application.resource.dto.CreateSingleDTO;
import ru.aizen.mtg.store.application.resource.dto.CreateStoreDTO;
import ru.aizen.mtg.store.application.resource.dto.ReserveSingleDTO;
import ru.aizen.mtg.store.application.resource.dto.response.StoreDTO;
import ru.aizen.mtg.store.application.resource.dto.response.Success;
import ru.aizen.mtg.store.application.service.FoundCard;
import ru.aizen.mtg.store.application.service.StoreService;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;
import ru.aizen.mtg.store.domain.store.StoreNotFountException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StoreResource {

	private final StoreService storeService;

	@Autowired
	public StoreResource(StoreService storeService) {
		this.storeService = storeService;
	}

	@PostMapping(path = "/create",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> create(@RequestBody CreateStoreDTO request) {
		storeService.create(request.getUserId(), request.getUsername(), request.getUserLocation(), request.getStoreName());
		return ResponseEntity.ok(
				new Success(HttpStatus.CREATED, "Store created")
		);
	}

	@GetMapping(path = "/{storeId}/edit",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StoreDTO> userStores(@PathVariable("storeId") String storeId,
	                                           @RequestHeader("X-UserId") Long userId) {
		Store store = storeService.find(storeId);

		storeService.permissionsToOperateByUserId(userId, storeId);

		return ResponseEntity.ok(StoreDTO.of(store));
	}

	@GetMapping(path = "/find/{userId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StoreDTO>> userStores(@PathVariable("userId") long userId) {
		Collection<Store> stores = storeService.stores(userId);

		return ResponseEntity.ok(stores.stream()
				.map(StoreDTO::of)
				.collect(Collectors.toList()));
	}

	@GetMapping(path = "/{owner}/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StoreDTO> view(@PathVariable("owner") String owner,
	                                     @PathVariable("name") String name) {
		Store store = storeService.view(owner, name);

		if (store == null) throw new StoreNotFountException(name, owner);

		StoreDTO storeDTO = StoreDTO.view(store);
		return ResponseEntity.ok(storeDTO);
	}

	@PutMapping(path = "/{storeId}/singles/add",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> addSingleToStore(@PathVariable("storeId") String storeId,
	                                                @RequestHeader("X-UserId") Long userId,
	                                                @RequestBody CreateSingleDTO request) {
		storeService.permissionsToOperateByUserId(userId, storeId);

		Single single = Single.create(request.getOracleId(), request.getOracleName())
				.printParameters(request.getName(), request.getSetCode(), request.getLangCode(), request.getStyle())
				.tradeParameters(request.getCondition(), request.getPrice(), request.getInStock());
		storeService.addSingle(storeId, single);
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Single added to store")
		);
	}

	@PutMapping(path = "/{storeId}/singles/import",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> importSinglesToStore(@PathVariable("storeId") String storeId,
	                                                    @RequestHeader("X-UserId") Long userId,
	                                                    @RequestPart("file") MultipartFile file) {
		storeService.permissionsToOperateByUserId(userId, storeId);

		storeService.addSingles(storeId, new ExcelSingleParser().singles(file));
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Singles imported to store")
		);
	}

	@PutMapping(path = "/{storeId}/singles/import/json",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> importJsonSinglesToStore(@PathVariable("storeId") String storeId,
	                                                        @RequestHeader("X-UserId") Long userId,
	                                                        @RequestBody String json) {
		storeService.permissionsToOperateByUserId(userId, storeId);

		storeService.addSingles(storeId, new JsonSingleParser().singles(json));
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Singles imported to store")
		);
	}

	@PutMapping(path = "/{storeId}/singles/{singleId}/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> editSingleInStore(@PathVariable("storeId") String storeId,
	                                                 @RequestHeader("X-UserId") Long userId,
	                                                 @PathVariable("singleId") String singleId,
	                                                 @RequestBody CreateSingleDTO request) {
		storeService.permissionsToOperateByUserId(userId, storeId);

		storeService.editSingle(storeId, singleId, request.getName(), request.getSetCode(), request.getLangCode(), request.getStyle(),
				request.getCondition(), request.getPrice(), request.getInStock());
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Single change saved")
		);
	}

	@DeleteMapping(path = "/{storeId}/singles/{singleId}/delete",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> deleteSingleFromStore(@PathVariable("storeId") String storeId,
	                                                     @RequestHeader("X-UserId") Long userId,
	                                                     @PathVariable("singleId") String singleId) {
		storeService.permissionsToOperateByUserId(userId, storeId);

		storeService.deleteSingle(storeId, singleId);
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Single deleted from store")
		);
	}

	@PutMapping(path = "/{storeId}/singles/reserve",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> reserveSinglesInStore(@PathVariable("storeId") String storeId,
	                                                     @RequestBody List<ReserveSingleDTO> request) {
		request.forEach(r -> storeService.reserveSingle(storeId, r.getSingleId(), r.getCount()));
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Singles reserved")
		);
	}

	@PostMapping(path = "/{storeId}/block",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> blockStore(@PathVariable("storeId") String storeId) {
		storeService.blockStore(storeId);
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Store disable")
		);
	}


	@PostMapping(path = "/{storeId}/unblock",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> unblockStore(@PathVariable("storeId") String storeId) {
		storeService.unblockStore(storeId);
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Singles enable")
		);
	}

	@DeleteMapping(path = "/{storeId}/delete",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Success> deleteStore(@PathVariable("storeId") String storeId,
	                                           @RequestHeader("X-UserId") Long userId) {
		storeService.permissionsToOperateByUserId(userId, storeId);

		storeService.removeStore(storeId);
		return ResponseEntity.ok(
				new Success(HttpStatus.OK, "Store deleted")
		);
	}

	@GetMapping(path = "/singles/{oracleId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<FoundCard> findByOracleID(@PathVariable("oracleId") String oracleId) {
		return storeService.findInStoresBySingleId(oracleId);
	}

}
