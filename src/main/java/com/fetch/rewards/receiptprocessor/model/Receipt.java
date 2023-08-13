package com.fetch.rewards.receiptprocessor.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


/**
 * Model for Receipt
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Receipt {

    /**
     * The name of the retailer or store the receipt is from
     */
    @NotBlank(message = "The retailer field is either absent or cannot have a null or empty value.")
    private String retailer;

    /**
     * The date of the purchase printed on the receipt.
     */
    @NotNull(message = "The purchase date field is either absent or cannot have a null or empty value.")
    private LocalDate purchaseDate;

    /**
     * The time of the purchase printed on the receipt.
     * 24-hour time expected.
     */
    @NotNull(message = "The purchase time field is either absent or cannot have a null or empty value.")
    private LocalTime purchaseTime;

    /**
     * List of items purchased
     */
    @Valid
    @NotNull(message = "The list of items is either absent or cannot have a null or empty value.")
    private List<Item> items;

    /**
     * The total amount paid on the receipt.
     */
    @NotNull(message = "The total field is either absent or cannot have a null or empty value.")
    @Min(value = 0, message = "The total value can't be a negative value")
    private Double total;

}
