package com.fetch.rewards.receiptprocessor.service;

/**
 * To store all the constants required for points calculation
 */
public class PriceConstants {

    public static final int POINTS_EARNED_ON_ROUND_TOTAL = 50;
    public static final int POINTS_EARNED_ON_TOTAL_MULTIPLE = 25;
    public static final int POINTS_EARNED_ON_EVERY_TWO_ITEMS = 5;
    public static final int POINTS_EARNED_ON_ODD_PURCHASE_DATE = 6;
    public static final int POINTS_EARNED_ON_PURCHASE_TIME = 10;
    /* Purchase time is in 24-hrs clock format. So 2:00 PM is 14 */
    public static final int MIN_PURCHASE_TIME = 14;
    /* Purchase time is in 24-hrs clock format. So 4:00 PM is 16 */
    public static final int MAX_PURCHASE_TIME = 16;
    public static final int ITEM_DESCRIPTION_MULTIPLE = 3;
    public static final double ITEM_PRICE_MULTIPLIER = 0.2;
    public static final double TOTAL_AMOUNT_MULTIPLE = 0.25;
}
