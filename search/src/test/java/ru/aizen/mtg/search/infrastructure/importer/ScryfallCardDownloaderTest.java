package ru.aizen.mtg.search.infrastructure.importer;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScryfallCardDownloaderTest {

	private static WireMockServer mockServer;

	@BeforeAll
	static void beforeAll() throws Exception {
		mockServer = new WireMockServer(4589);
	}

	@BeforeEach
	void setUp() {
		mockServer.start();
	}

	@AfterEach
	void tearDown() {
		mockServer.stop();
	}

	@Test
	public void cardDataFileIsActual() throws Exception {
		Path cardsData = Paths.get("data_test.json");
		Files.write(cardsData, "old_data".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		String requestUri = "http://localhost:4589/bulk-data/oracle_cards";
		String content = "{" +
				"\"updated_at\":\"2021-01-01T00:00:00.000+00:00\"," +
				"\"download_uri\":\"http://localhost:4589/oracle_cards.json\"" +
				"}";

		mockServer.stubFor(get("/bulk-data/oracle_cards")
				.willReturn(aResponse()
						.withStatus(HttpStatus.SC_OK)
						.withBody(content)));

		new ScryfallCardDownloader(requestUri).downloadDataTo(cardsData);

		assertEquals("old_data", Files.readAllLines(cardsData).get(0));

		Files.delete(cardsData);
	}

}