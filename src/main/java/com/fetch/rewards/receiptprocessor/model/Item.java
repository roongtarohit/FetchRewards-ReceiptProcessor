package com.fetch.rewards.receiptprocessor.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for item
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /**
     * Short Product Description for the item
     */
    @NotBlank(message = "The description of an item is either absent or cannot have a null or empty value.")
    private String shortDescription;

    /**
     * The total price paid for this item.
     */
    @NotNull(message = "The price of an item is either absent or cannot have a null or empty value.")
    @Min(value = 0, message = "The price of an item can't be negative")
    private Double price;
}
