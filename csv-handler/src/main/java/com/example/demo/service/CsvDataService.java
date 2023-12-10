package com.example.demo.service;

import com.example.demo.data.CsvData;
import com.example.demo.exception.CsvProcessingException;
import com.example.demo.repository.CsvDataRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CsvDataService {

    private final CsvDataRepository csvDataRepository;
    private static final String HEADER = "source,\"codeListCode\",\"code\",\"displayValue\",\"longDescription\",\"fromDate\",\"toDate\",\"sortingPriority\"\n";
    private static final String SEPARATOR = "\",\"";
    @Autowired
    public CsvDataService(CsvDataRepository csvDataRepository) {
        this.csvDataRepository = csvDataRepository;
    }

    public List<CsvData> getAllCsvData() {
        return csvDataRepository.findAll();
    }

    public StreamingResponseBody getStreamAllCsvData() {
        List<CsvData> csvDataList = csvDataRepository.findAll();

        return out -> {
            try (Writer writer = new OutputStreamWriter(out)) {
                writer.write(HEADER);

                for (CsvData csvData : csvDataList) {
                    writer.write(csvData.getSource() + ",\"");
                    writer.write(csvData.getCodeListCode() + SEPARATOR);
                    writer.write( csvData.getCode() + SEPARATOR);
                    writer.write(csvData.getDisplayValue() + SEPARATOR);
                    writer.write(csvData.getLongDescription() + SEPARATOR);
                    writer.write(DateUtils.formatDate(csvData.getFromDate()) + SEPARATOR);
                    writer.write(DateUtils.formatDate(csvData.getToDate()) + SEPARATOR);
                    writer.write(csvData.getSortingPriority() + "\"\n");
                }
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    public Optional<String> getCsvDataByCode(String code) {
        Optional<CsvData> csvData = csvDataRepository.findById(code);

        if (csvData.isPresent()) {
            String csvContent = HEADER;

            csvContent = csvContent + csvData.get().getSource() + ",\"" +
                    csvData.get().getCodeListCode() + SEPARATOR + csvData.get().getCode() + SEPARATOR +
                    csvData.get().getDisplayValue() + SEPARATOR + csvData.get().getLongDescription() + SEPARATOR + DateUtils.formatDate(csvData.get().getFromDate()) + SEPARATOR +
                    DateUtils.formatDate(csvData.get().getToDate()) + SEPARATOR + csvData.get().getSortingPriority() + "\"\n";
            return Optional.of(csvContent);
        } else {
            return Optional.empty();
        }
    }

    public void processCsvFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);
            csvReader.readNext();

            List<CsvData> csvDataList = parseCsvToData(csvReader);
            csvDataRepository.saveAll(csvDataList);
        } catch (IOException | CsvValidationException e) {
            throw new CsvProcessingException("Error processing CSV file: " + e.getMessage(), e);
        }
    }

    private List<CsvData> parseCsvToData(CSVReader csvReader) throws CsvValidationException, IOException {
        String[] line;
        List<CsvData> csvDataList = new ArrayList<>();

        while ((line = csvReader.readNext()) != null) {
            CsvData csvData = createCsvDataFromLine(line);
            csvDataList.add(csvData);
        }
        return csvDataList;
    }

    private CsvData createCsvDataFromLine(String[] line) {
        CsvData csvData = new CsvData();
        csvData.setCode(line[2]);
        csvData.setSource(line[0]);
        csvData.setCodeListCode(line[1]);
        csvData.setDisplayValue(line[3]);
        csvData.setLongDescription(line[4]);
        csvData.setFromDate(DateUtils.parseDate(line[5]));
        csvData.setToDate(DateUtils.parseDate(line[6]));
        csvData.setSortingPriority(line[7]);
        return csvData;
    }

    public void deleteAllCsvData() {
        csvDataRepository.deleteAll();
    }
}

