package com.fetch.rewards.receiptprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Model for Points response corresponds to getPoints() API
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PointsResponse {

    /**
     * Points awarded for the receipt
     */
    private Long points;

    /**
     * Store error message
     */
    private String error;

}
