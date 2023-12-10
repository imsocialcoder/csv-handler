package com.example.demo.controller;

import com.example.demo.service.CsvDataService;
import com.example.demo.service.CsvHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Optional;

@RestController
@RequestMapping("/csvdata")
public class CsvDataController {

    private final CsvDataService csvDataService;

    @Autowired
    public CsvDataController(CsvDataService csvDataService) {
        this.csvDataService = csvDataService;
    }

    @GetMapping("/all")
    public ResponseEntity<StreamingResponseBody> downloadAllCsvData() {
        StreamingResponseBody stream = csvDataService.getStreamAllCsvData();
        HttpHeaders headers = CsvHeadersUtil.createCsvHeaders("data.csv");
        return ResponseEntity.ok().headers(headers).body(stream);
    }

    @GetMapping("/{code}")
    public ResponseEntity<String> getCsvDataByCode(@PathVariable String code) {
        Optional<String> csvContent = csvDataService.getCsvDataByCode(code);

        if (csvContent.isPresent()) {
            String filename = "data_for_" + code + ".csv";
            HttpHeaders headers = CsvHeadersUtil.createCsvHeaders(filename);
            return ResponseEntity.ok().headers(headers).body(csvContent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file!");
        }

        try {
            csvDataService.processCsvFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded and data saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllCsvData() {
        csvDataService.deleteAllCsvData();
        return ResponseEntity.ok("All CSV data deleted successfully");
    }
}