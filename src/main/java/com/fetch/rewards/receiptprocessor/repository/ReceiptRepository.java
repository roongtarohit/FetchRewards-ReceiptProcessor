package com.fetch.rewards.receiptprocessor.repository;

import com.fetch.rewards.receiptprocessor.model.Receipt;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Receipt repository to store receipt based on receipt id
 */
@Repository
@Getter
public class ReceiptRepository {
    final private Map<String, Receipt> receiptRepository;

    public ReceiptRepository() {
        this.receiptRepository = new HashMap<>();
    }

    /**
     * To get the receipt for the id
     * @param receiptId id
     * @return Optional of Receipt
     */
    public Optional<Receipt> getReceipt(final String receiptId) {
        return Optional.ofNullable(this.receiptRepository.get(receiptId));
    }

    /**
     * To store/save the receipt
     * @param receiptId id
     * @param receipt receipt
     */
    public void saveReceipt(final String receiptId,
                            final Receipt receipt) {
        this.receiptRepository.put(receiptId, receipt);
    }

}
