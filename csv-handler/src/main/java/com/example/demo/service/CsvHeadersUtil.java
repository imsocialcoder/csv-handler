package com.example.demo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CsvHeadersUtil {
    public static HttpHeaders createCsvHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", filename);
        return headers;
    }
}