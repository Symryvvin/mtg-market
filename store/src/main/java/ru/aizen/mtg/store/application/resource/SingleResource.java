package ru.aizen.mtg.store.application.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.aizen.mtg.store.application.parser.ExcelSingleParser;
import ru.aizen.mtg.store.application.parser.JsonSingleParser;
import ru.aizen.mtg.store.application.resource.dto.request.CreateSingleDTO;
import ru.aizen.mtg.store.application.resource.dto.request.ReserveSingleDTO;
import ru.aizen.mtg.store.application.resource.dto.response.FoundSingle;
import ru.aizen.mtg.store.application.service.StoreService;
import ru.aizen.mtg.store.domain.single.Single;
import ru.aizen.mtg.store.domain.store.Store;

import java.util.List;

@RestController
@RequestMapping("/rest/single")
public class SingleResource {

	private final StoreService storeService;

	@Autowired
	public SingleResource(StoreService storeService) {
		this.storeService = storeService;
	}

	@PutMapping(path = "/add",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> addSingleToStore(@RequestHeader("X-UserId") Long userId,
	                                             @RequestBody CreateSingleDTO request) {
		Single single = Single.create(request.getOracleId(), request.getOracleName())
				.printParameters(request.getName(), request.getSetCode(), request.getLangCode(), request.getStyle())
				.tradeParameters(request.getCondition(), request.getPrice(), request.getInStock());

		Store store = storeService.store(userId);

		storeService.addSingle(store, single);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/import/excel",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> importSinglesToStore(@RequestHeader("X-UserId") Long userId,
	                                                 @RequestPart("file") MultipartFile file) {
		Store store = storeService.store(userId);

		storeService.addSingles(store, new ExcelSingleParser().singles(file));
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/import/json",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> importJsonSinglesToStore(@RequestHeader("X-UserId") Long userId,
	                                                     @RequestBody String json) {
		Store store = storeService.store(userId);

		storeService.addSingles(store, new JsonSingleParser().singles(json));
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/edit/{singleId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> editSingleInStore(@RequestHeader("X-UserId") Long userId,
	                                              @PathVariable("singleId") String singleId,
	                                              @RequestBody CreateSingleDTO request) {
		Store store = storeService.store(userId);

		storeService.editSingle(store, singleId,
				request.getName(), request.getSetCode(), request.getLangCode(), request.getStyle(),
				request.getCondition(), request.getPrice(), request.getInStock());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(path = "/delete/{singleId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteSingleFromStore(@RequestHeader("X-UserId") Long userId,
	                                                  @PathVariable("singleId") String singleId) {
		Store store = storeService.store(userId);

		storeService.deleteSingle(store, singleId);
		return ResponseEntity.ok().build();
	}

	@PutMapping(path = "/reserve/{traderId}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> reserveSinglesInStore(@PathVariable("traderId") Long traderId,
	                                                  @RequestBody List<ReserveSingleDTO> request) {
		Store store = storeService.store(traderId);

		request.forEach(r -> storeService.reserveSingle(store, r.getSingleId(), r.getCount()));
		return ResponseEntity.ok().build();
	}


	@GetMapping(path = "/search/{oracleId}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<FoundSingle> findByOracleID(@PathVariable("oracleId") String oracleId) {
		return CollectionModel.of(storeService.findInStoresBySingleId(oracleId));
	}

}
