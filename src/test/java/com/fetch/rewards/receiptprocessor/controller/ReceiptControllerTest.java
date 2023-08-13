package com.fetch.rewards.receiptprocessor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetch.rewards.receiptprocessor.model.PointsResponse;
import com.fetch.rewards.receiptprocessor.model.Receipt;
import com.fetch.rewards.receiptprocessor.model.ReceiptResponse;
import com.fetch.rewards.receiptprocessor.repository.ReceiptRepository;
import com.fetch.rewards.receiptprocessor.service.ReceiptProcessorService;
import com.fetch.rewards.receiptprocessor.utils.ReceiptUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static com.fetch.rewards.receiptprocessor.controller.ReceiptController.BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for Receipt Controller
 */
@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {

    private static final String VALID_RECEIPT_ID = "validReceiptId";
    private static final String INVALID_RECEIPT_ID = "invalidReceiptId";
    private static final long POINTS = 2502;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReceiptProcessorService mockReceiptProcessor;

    @MockBean
    private Receipt mockReceipt;

    @MockBean
    private ReceiptRepository mockReceiptRepo;

    @InjectMocks
    private ReceiptController classUnderTest;

    @Test
    public void Given_invalidPurchaseDate_When_processReceipt_Return_badRequestStatusCode() throws Exception {
        final String inputJson =
                ReceiptUtil.readFromFileToString("receipt-test-data/invalid-receipts/invalid-date.json");
        final String uri = String.format("%s/%s", BASE_URL, "process");

        final String response = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(inputJson))
                                       .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final ReceiptResponse actualResponse = objectMapper.readValue(response, ReceiptResponse.class);

        assertNull(actualResponse.getReceiptId());
        assertNotNull(actualResponse.getErrors());
        assertEquals(1, actualResponse.getErrors().size());
    }

    @Test
    public void Given_invalidPurchaseTime_When_processReceipt_Return_badRequestStatusCode() throws Exception {
        final String inputJson =
                ReceiptUtil.readFromFileToString("receipt-test-data/invalid-receipts/invalid-time.json");
        final String uri = String.format("%s/%s", BASE_URL, "process");

        final String response = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(inputJson))
                                       .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final ReceiptResponse actualResponse = objectMapper.readValue(response, ReceiptResponse.class);

        assertNull(actualResponse.getReceiptId());
        assertNotNull(actualResponse.getErrors());
        assertEquals(1, actualResponse.getErrors().size());
    }

    @Test
    public void Given_invalidReceipt_When_processReceipt_Return_badRequestStatusCode() throws Exception {
        // Empty retailer name, a couple of items with negative price and negative receipt total
        final String inputJson =
                ReceiptUtil.readFromFileToString("receipt-test-data/invalid-receipts/invalid-receipt.json");
        final String uri = String.format("%s/%s", BASE_URL, "process");

        final String response = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(inputJson))
                                       .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final ReceiptResponse actualResponse = objectMapper.readValue(response, ReceiptResponse.class);

        assertNull(actualResponse.getReceiptId());
        assertNotNull(actualResponse.getErrors());
        assertEquals(4, actualResponse.getErrors().size());
    }

    @Test
    public void Given_emptyItems_When_processReceipt_Return_badRequestStatusCode() throws Exception {
        final String inputJson =
                ReceiptUtil.readFromFileToString("receipt-test-data/invalid-receipts/empty-items.json");
        final String uri = String.format("%s/%s", BASE_URL, "process");

        final String response = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(inputJson))
                                       .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final ReceiptResponse actualResponse = objectMapper.readValue(response, ReceiptResponse.class);

        assertNull(actualResponse.getReceiptId());
        assertNotNull(actualResponse.getErrors());
        assertEquals(1, actualResponse.getErrors().size());
    }

    @Test
    public void Given_validReceipt1_When_processReceipt_Return_badRequestStatusCode() throws Exception {
        final String inputJson =
                ReceiptUtil.readFromFileToString("receipt-test-data/valid-receipts/receipt1.json");
        final String uri = String.format("%s/%s", BASE_URL, "process");

        final String response = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(inputJson))
                                       .andExpect(MockMvcResultMatchers.status().isOk())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final ReceiptResponse actualResponse = objectMapper.readValue(response, ReceiptResponse.class);

        assertNotNull(actualResponse.getReceiptId());
        assertNull(actualResponse.getErrors());
    }

    @Test
    public void Given_validReceipt2_When_processReceipt_Return_badRequestStatusCode() throws Exception {
        final String inputJson =
                ReceiptUtil.readFromFileToString("receipt-test-data/valid-receipts/receipt2.json");
        final String uri = String.format("%s/%s", BASE_URL, "process");

        final String response = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(inputJson))
                                       .andExpect(MockMvcResultMatchers.status().isOk())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final ReceiptResponse actualResponse = objectMapper.readValue(response, ReceiptResponse.class);

        assertNotNull(actualResponse.getReceiptId());
        assertNull(actualResponse.getErrors());
    }

    @Test
    public void Given_validReceiptId_When_getPoints_Return_points() throws Exception {

        when(mockReceiptRepo.getReceipt(VALID_RECEIPT_ID)).thenReturn(Optional.of(mockReceipt));
        when(mockReceiptProcessor.getPoints(mockReceipt)).thenReturn(POINTS);

        final String uri = String.format("%s/%s/%s", BASE_URL, VALID_RECEIPT_ID, "points");

        final String response = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                                       .andExpect(MockMvcResultMatchers.status().isOk())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final PointsResponse actualResponse = objectMapper.readValue(response, PointsResponse.class);

        assertEquals(POINTS, actualResponse.getPoints());
    }

    @Test
    public void Given_invalidReceiptId_When_getPoints_Return_notFoundStatusCode() throws Exception {

        when(mockReceiptRepo.getReceipt(INVALID_RECEIPT_ID)).thenReturn(Optional.empty());

        final String uri = String.format("%s/%s/%s", BASE_URL, VALID_RECEIPT_ID, "points");

        final String response = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                                       .andExpect(MockMvcResultMatchers.status().isNotFound())
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        final PointsResponse actualResponse = objectMapper.readValue(response, PointsResponse.class);

        assertNull(actualResponse.getPoints());
        assertNotNull(actualResponse.getError());
    }

}