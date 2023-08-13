package com.fetch.rewards.receiptprocessor.service;

import com.fetch.rewards.receiptprocessor.model.Item;
import com.fetch.rewards.receiptprocessor.model.Receipt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.fetch.rewards.receiptprocessor.service.PriceConstants.ITEM_DESCRIPTION_MULTIPLE;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.ITEM_PRICE_MULTIPLIER;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.MAX_PURCHASE_TIME;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.MIN_PURCHASE_TIME;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_EVERY_TWO_ITEMS;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_ODD_PURCHASE_DATE;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_PURCHASE_TIME;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_ROUND_TOTAL;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_TOTAL_MULTIPLE;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.TOTAL_AMOUNT_MULTIPLE;

/**
 * Receipt processor to calculate the points for a receipt
 */
@Slf4j
@Service
public class ReceiptProcessorService {

    /**
     * To calculate the points awarded to a receipt
     * @param receipt Receipt
     * @return points earned from the receipt
     */
    public long getPoints(final Receipt receipt) {
        long points = 0;

        points +=  getPointsForRetailerName(receipt.getRetailer());
        points += getPointsForReceiptTotal(receipt.getTotal());
        points += getPointsForReceiptItems(receipt.getItems());
        points += getPointsForPurchaseDate(receipt.getPurchaseDate());
        points += getPointsForPurchaseTime(receipt.getPurchaseTime());

        return points;
    }

    /**
     * One point for every alphanumeric character in the retailer name.
     * @param retailer Retailer name
     * @return points earned from retailer name
     */
    private long getPointsForRetailerName(final String retailer) {
        final long points = retailer.chars().filter(Character::isLetterOrDigit).count();
        log.info("Points earned from retailer name : " + points);
        return points;
    }

    /**
     * 50 points if the total is a round dollar amount with no cents.
     * 25 points if the total is a multiple of "0.25"
     * @param receiptTotal receipt amount
     * @return points earned from the receipt amount
     */
    private long getPointsForReceiptTotal(final Double receiptTotal) {
        long points = 0;
        // Every whole number is going to be a multiple of 0.25
        if (isRoundAmount(receiptTotal)) {
            points += POINTS_EARNED_ON_ROUND_TOTAL;
        }

        if (receiptTotal % TOTAL_AMOUNT_MULTIPLE == 0) {
            points += POINTS_EARNED_ON_TOTAL_MULTIPLE;
        }
        log.info("Points earned from the receipt amount : " + points);
        return points;
    }

    /**
     * Check if the amount is a whole number
     * @param receiptTotal amount
     * @return boolean
     */
    private boolean isRoundAmount(final double receiptTotal) {
        return receiptTotal == (int) receiptTotal;
    }

    /**
     * 5 points for every two items on the receipt.
     * If the trimmed length of the item description is a multiple of 3,
     * multiply the price by 0.2 and round up to the nearest integer.
     * The result is the number of points earned.
     * @param items List of items
     * @return points earned for items
     */
    private long getPointsForReceiptItems(final List<Item> items) {
        long points = 0;

        final int numOfItems = items.size();
        points += ((long) (numOfItems / 2) * POINTS_EARNED_ON_EVERY_TWO_ITEMS);

        for (Item item: items) {
            final String itemDescription = item.getShortDescription().trim();
            final double itemPrice = item.getPrice();
            if (itemDescription.length() % ITEM_DESCRIPTION_MULTIPLE == 0) {
                points += (long) Math.ceil(itemPrice * ITEM_PRICE_MULTIPLIER);
            }
        }
        log.info("Points earned from the items : " + points);
        return points;
    }

    /**
     * 6 points if the day in the purchase date is odd.
     * @param purchaseDate purchase date
     * @return points earned from date
     */
    private long getPointsForPurchaseDate(final LocalDate purchaseDate) {
        long points = 0;
        // SimpleDateFormat uses the system's default time zone, which could lead to unexpected results in some cases.
        final int day = purchaseDate.getDayOfMonth();

        if (day % 2 == 1) {
            points += POINTS_EARNED_ON_ODD_PURCHASE_DATE;
        }

        log.info("Points earned from purchase date : " + points);

        return points;
    }

    /**
     * 10 points if the time of purchase is after 2:00pm and before 4:00pm.
     * @param purchaseTime time of purchase
     * @return points earned from purchase time
     */
    private long getPointsForPurchaseTime(final LocalTime purchaseTime) {
        long points = 0;
        final LocalTime startTime = LocalTime.of(MIN_PURCHASE_TIME, 0); // 2 PM
        final LocalTime endTime = LocalTime.of(MAX_PURCHASE_TIME, 0);   // 4 PM
        if (purchaseTime.isAfter(startTime) && purchaseTime.isBefore(endTime)) {
            points += POINTS_EARNED_ON_PURCHASE_TIME;
        }
        log.info("Points earned from purchase time : " + points);
        return points;
    }
}
