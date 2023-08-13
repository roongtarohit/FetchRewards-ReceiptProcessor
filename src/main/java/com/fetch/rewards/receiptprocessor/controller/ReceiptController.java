package com.fetch.rewards.receiptprocessor.controller;

import com.fetch.rewards.receiptprocessor.model.PointsResponse;
import com.fetch.rewards.receiptprocessor.model.Receipt;
import com.fetch.rewards.receiptprocessor.model.ReceiptResponse;
import com.fetch.rewards.receiptprocessor.repository.ReceiptRepository;
import com.fetch.rewards.receiptprocessor.service.ReceiptProcessorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

/**
 * Receipt Controller
 */
@Slf4j
@RestController
@RequestMapping(value = ReceiptController.BASE_URL)
public class ReceiptController {

    public static final String BASE_URL = "/receipts";

    private final ReceiptRepository receiptRepository;
    private final ReceiptProcessorService receiptProcessor;

    @Autowired
    public ReceiptController(final ReceiptRepository receiptRepository,
                             final ReceiptProcessorService receiptProcessor) {
        this.receiptRepository = receiptRepository;
        this.receiptProcessor = receiptProcessor;
    }

    @PostMapping(value = "/process", produces = "application/json")
    public ResponseEntity<ReceiptResponse> processReceipt(@Valid @RequestBody Receipt receipt) {
        final String receiptId = UUID.randomUUID().toString();
        receiptRepository.saveReceipt(receiptId, receipt);
        return new ResponseEntity<>(ReceiptResponse.builder()
                                                   .receiptId(receiptId)
                                                   .build(),
                                    HttpStatus.OK);
    }

    @GetMapping(value = "{receiptId}/points", produces = "application/json")
    public ResponseEntity<PointsResponse> getPoints(@PathVariable("receiptId") String receiptId) {
        final Optional<Receipt> receipt = receiptRepository.getReceipt(receiptId);
        System.out.println(receiptId);
        System.out.println(receipt);
        if (receipt.isEmpty()) {
            return new ResponseEntity<>(PointsResponse.builder()
                                                      .error("No receipt found for the id :" + receiptId)
                                                      .build(),
                                        HttpStatus.NOT_FOUND);
        }
        log.info("Calculating points for receipt id : " + receiptId);
        System.out.println(receiptId);
        System.out.println(receiptProcessor.getPoints(receipt.get()));
        return new ResponseEntity<>(PointsResponse.builder()
                                                  .points(receiptProcessor.getPoints(receipt.get()))
                                                  .build(),
                                    HttpStatus.OK);
    }
}
