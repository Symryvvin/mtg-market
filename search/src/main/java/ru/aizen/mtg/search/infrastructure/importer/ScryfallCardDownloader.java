
package ru.aizen.mtg.search.infrastructure.importer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.aizen.mtg.search.domain.importer.CardDownloader;
import ru.aizen.mtg.search.domain.importer.CardImporterException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class ScryfallCardDownloader implements CardDownloader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScryfallCardDownloader.class);

	private static final String DOWNLOAD_URI = "download_uri";
	private static final String UPDATED_AT = "updated_at";

	private final String cardDataRemote;

	public ScryfallCardDownloader(@Value("${card.data.remote}") String cardDataRemote) {
		this.cardDataRemote = cardDataRemote;
	}

	@Override
	public Path downloadDataTo(Path cardsDataPath) throws CardImporterException {
		try {
			JsonNode bulkData = bulkData();
			LocalDateTime updatedAt = LocalDateTime.parse(bulkData.get(UPDATED_AT).asText().replaceAll("/+", ""), DateTimeFormatter.ISO_ZONED_DATE_TIME);

			if (isCardDataOutdatedOrMissing(cardsDataPath, updatedAt)) {
				LOGGER.info("Card data is outdated. Try get new bulk data from scryfall.com");
				InputStream is = HttpClients.createDefault()
						.execute(new HttpGet(bulkData.get(DOWNLOAD_URI).asText()))
						.getEntity()
						.getContent();

				if (!Files.exists(cardsDataPath.getParent())) {
					Files.createDirectories(cardsDataPath.getParent());
				}

				Files.copy(is, cardsDataPath, StandardCopyOption.REPLACE_EXISTING);
			}

			return cardsDataPath;
		} catch (IOException e) {
			throw new CardImporterException("", e);
		}
	}

	private JsonNode bulkData() throws CardImporterException {
		try {
			HttpEntity entity = HttpClients.createDefault()
					.execute(new HttpGet(cardDataRemote))
					.getEntity();

			return new ObjectMapper().readTree(EntityUtils.toString(entity));
		} catch (IOException e) {
			throw new CardImporterException("Error while execute request  " + cardDataRemote, e);
		}
	}

	private boolean isCardDataOutdatedOrMissing(Path cardData, LocalDateTime actualDataUpdatedAt) throws CardImporterException {
		if (Files.exists(cardData)) {
			return actualDataUpdatedAt.isAfter(fileCreationDate(cardData));
		}
		return true;
	}

	private LocalDateTime fileCreationDate(Path file) throws CardImporterException {
		try {
			BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
			return LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());
		} catch (IOException e) {
			throw new CardImporterException("Error while read file [" + file + "] attributes ", e);
		}
	}


}
