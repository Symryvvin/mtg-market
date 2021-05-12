package ru.aizen.mtg.store.application.resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.aizen.mtg.store.application.resource.exception.SingleParserException;
import ru.aizen.mtg.store.domain.single.Single;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SingleParser {

	private static final int FIRST_READ_ROW = 1;

	public Collection<Single> singles(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream();
		     Workbook book = WorkbookFactory.create(inputStream)) {
			List<Single> result = new ArrayList<>();

			Sheet sheet = book.getSheetAt(0);
			int lastRowNumber = sheet.getLastRowNum();

			for (int i = FIRST_READ_ROW; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				Single single = Single.create(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue())
						.printParameters(
								row.getCell(2).getStringCellValue(),
								row.getCell(3).getStringCellValue(),
								row.getCell(4).getStringCellValue(),
								row.getCell(5).getStringCellValue())
						.tradeParameters(
								row.getCell(6).getStringCellValue(),
								row.getCell(7).getNumericCellValue(),
								(int) row.getCell(8).getNumericCellValue());
				result.add(single);
			}
			return result;
		} catch (IOException e) {
			throw new SingleParserException("Can`t parse excel file with singles", e);
		}

	}
}
