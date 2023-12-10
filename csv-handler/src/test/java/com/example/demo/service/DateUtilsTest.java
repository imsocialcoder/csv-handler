package com.example.demo.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DateUtilsTest {
    @Test
    public void testParseValidDate() {
        String validDateString = "31-12-2023";
        LocalDate parsedDate = DateUtils.parseDate(validDateString);
        assertEquals(LocalDate.of(2023, 12, 31), parsedDate);
    }

    @Test
    public void testParseEmptyDate() {
        String emptyDateString = "";
        LocalDate emptyParsedDate = DateUtils.parseDate(emptyDateString);
        assertNull(emptyParsedDate);
    }

    @Test
    public void testFormatValidDate() {
        LocalDate validDate = LocalDate.of(2023, 12, 31);
        String formattedDate = DateUtils.formatDate(validDate);
        assertEquals("31-12-2023", formattedDate);
    }

    @Test
    public void testFormatNullDate() {
        LocalDate nullDate = null;
        String formattedNullDate = DateUtils.formatDate(nullDate);
        assertEquals("", formattedNullDate);
    }
}
