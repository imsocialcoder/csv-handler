package com.example.demo.service;

import com.example.demo.data.CsvData;
import com.example.demo.repository.CsvDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CsvDataServiceTest {

    @Mock
    private CsvDataRepository csvDataRepository;

    @InjectMocks
    private CsvDataService csvDataService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCsvData() {
        List<CsvData> expectedData = new ArrayList<>();
        expectedData.add(new CsvData("ZIB", "ZIB001", "271636001", "Polsslag regelmatig", "The long description is necessary", null, null, "1"));
        expectedData.add(new CsvData("ZIB", "ZIB002", "Type 2", "Als een worst, maar klonterig", null, null, null, null));

        when(csvDataRepository.findAll()).thenReturn(expectedData);

        List<CsvData> actualData = csvDataService.getAllCsvData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testDeleteAllCsvData() {
        csvDataService.deleteAllCsvData();

        verify(csvDataRepository, times(1)).deleteAll();
    }
}

