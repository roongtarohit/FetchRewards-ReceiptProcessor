package com.fetch.rewards.receiptprocessor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fetch.rewards.receiptprocessor.model.Receipt;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Receipt Util to read json object from file.
 */
public class ReceiptUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Receipt deserializeReceipt(String filePath) throws IOException {
        // To support Java 8's java.time types, like LocalDate, LocalTime
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(readFromFileToString(filePath), Receipt.class);
    }
    public static String readFromFileToString(String filePath) throws IOException {
        final File resource = new ClassPathResource(filePath).getFile();
        final byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }
}
