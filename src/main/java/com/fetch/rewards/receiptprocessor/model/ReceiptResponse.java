package com.fetch.rewards.receiptprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Model for Points response corresponds to process() API
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReceiptResponse {

    /**
     * The unique receipt id
     */
    private String receiptId;

    /**
     * To store all the validation related errors
     */
    private List<String> errors;

}
