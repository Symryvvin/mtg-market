package ru.aizen.mtg.search.domain.importer;

import java.nio.file.Path;

public interface CardDownloader {

	Path downloadDataTo(Path cardsDataPath) throws CardImporterException;

}
