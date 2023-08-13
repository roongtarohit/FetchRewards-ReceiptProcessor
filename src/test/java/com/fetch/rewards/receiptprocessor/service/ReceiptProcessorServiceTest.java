package com.fetch.rewards.receiptprocessor.service;

import com.fetch.rewards.receiptprocessor.model.Item;
import com.fetch.rewards.receiptprocessor.model.Receipt;
import com.fetch.rewards.receiptprocessor.utils.ReceiptUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.fetch.rewards.receiptprocessor.service.PriceConstants.MAX_PURCHASE_TIME;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.MIN_PURCHASE_TIME;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_ODD_PURCHASE_DATE;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_PURCHASE_TIME;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_ROUND_TOTAL;
import static com.fetch.rewards.receiptprocessor.service.PriceConstants.POINTS_EARNED_ON_TOTAL_MULTIPLE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Receipt Processor
 */
@SpringJUnitConfig(classes = ReceiptProcessorService.class)
class ReceiptProcessorServiceTest {

    private static final long ZERO_POINTS = 0;

    @Autowired
    private ReceiptProcessorService classUnderTest;

    @Test
    public void Given_receipt1_When_getPoints_Return_points() throws IOException {
        final Receipt receipt = ReceiptUtil.deserializeReceipt("receipt-test-data/valid-receipts/receipt1.json");
        final Long actualPoints = classUnderTest.getPoints(receipt);
        assertEquals(28, actualPoints);

    }

    @Test
    public void Given_receipt2_When_getPoints_Return_points() throws IOException {
        final Receipt receipt = ReceiptUtil.deserializeReceipt("receipt-test-data/valid-receipts/receipt2.json");
        final Long actualPoints = classUnderTest.getPoints(receipt);
        assertEquals(109, actualPoints);

    }

    @Test
    public void Given_retailerName_When_getPointsForRetailerName_Return_alphaNumericCount() {
        final String retailer = "Target";
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForRetailerName",
                                                                   retailer);
        assertEquals(6, actualPoints);
    }

    @Test
    public void Given_retailerNameWithSpecialCharacters_When_getPointsForRetailerName_Return_alphaNumericCount() {
        final String retailer = "M&M Corner Market";
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForRetailerName",
                                                                   retailer);
        assertEquals(14, actualPoints);
    }

    @Test
    public void Given_receiptTotalAsWhole_When_getPointsForReceiptTotal_Return_points() {
        // Every whole number is a multiple of 0.25
        final double receiptAmount = 13;
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptTotal",
                                                                   receiptAmount);
        assertEquals(POINTS_EARNED_ON_ROUND_TOTAL + POINTS_EARNED_ON_TOTAL_MULTIPLE,
                     actualPoints);
    }

    @Test
    public void Given_receiptTotalAsMultipleAndNotWholeNumber_When_getPointsForReceiptTotal_Return_pointsEarnedOnMultiple() {
        final double receiptAmount = 13.25;
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptTotal",
                                                                   receiptAmount);
        assertEquals(POINTS_EARNED_ON_TOTAL_MULTIPLE,
                     actualPoints);
    }

    @Test
    public void Given_receiptTotalNotMultipleAndNotWholeNumber_When_getPointsForReceiptTotal_Return_zeroPoints() {
        final double receiptAmount = 13.15;
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptTotal",
                                                                   receiptAmount);
        assertEquals(ZERO_POINTS, actualPoints);
    }

    @Test
    public void Given_oddItems_When_getPointsForReceiptItems_Return_zeroPoints() {
        final List<Item> items = new ArrayList<>();
        items.add(Item.builder().shortDescription("Item").price(12.0).build());
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptItems",
                                                                   items);
        assertEquals(ZERO_POINTS, actualPoints);
    }

    @Test
    public void Given_oddItemAndTrimmedLengthIsMultiple_When_getPointsForReceiptItems_Return_points() {
        final List<Item> items = new ArrayList<>();
        items.add(Item.builder().shortDescription("Ite ").price(11.0).build());
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptItems",
                                                                   items);
        assertEquals(3, actualPoints);
    }

    @Test
    public void Given_evenItems_When_getPointsForReceiptItems_Return_points() {
        final List<Item> items = new ArrayList<>();
        items.add(Item.builder().shortDescription("Item").price(11.0).build());
        items.add(Item.builder().shortDescription("Item").price(11.0).build());
        items.add(Item.builder().shortDescription("Item").price(11.0).build());
        items.add(Item.builder().shortDescription("Item").price(11.0).build());
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptItems",
                                                                   items);
        assertEquals(10, actualPoints);
    }

    @Test
    public void Given_evenItemsAndTrimmedLengthIsMultiple_When_getPointsForReceiptItems_Return_points() {
        final List<Item> items = new ArrayList<>();
        items.add(Item.builder().shortDescription("Ite ").price(11.0).build());
        items.add(Item.builder().shortDescription("Ite ").price(11.0).build());
        items.add(Item.builder().shortDescription("Ite ").price(11.0).build());
        items.add(Item.builder().shortDescription("Ite ").price(11.0).build());
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForReceiptItems",
                                                                   items);
        assertEquals(22, actualPoints);
    }

    @Test
    public void Given_oddPurchaseDate_When_getPointsForPurchaseDate_Return_pointsEarnedForOddDate() {
        final LocalDate oddPurchaseDate = LocalDate.of(2023, 8, 13);
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForPurchaseDate",
                                                                   oddPurchaseDate);
        assertEquals(POINTS_EARNED_ON_ODD_PURCHASE_DATE, actualPoints);
    }

    @Test
    public void Given_evenPurchaseDate_When_getPointsForPurchaseDate_Return_zeroPoints() {
        final LocalDate oddPurchaseDate = LocalDate.of(2023, 8, 12);
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForPurchaseDate",
                                                                   oddPurchaseDate);
        assertEquals(ZERO_POINTS, actualPoints);
    }

    @Test
    public void Given_purchaseTimeWithinTimeFrame_When_getPointsForPurchaseTime_Return_pointsEarnedOnPurchaseTime() {
        final LocalTime purchaseTime = LocalTime.of(14, 1);
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForPurchaseTime",
                                                                   purchaseTime);
        assertEquals(POINTS_EARNED_ON_PURCHASE_TIME, actualPoints);
    }

    @Test
    public void Given_purchaseTimeIs6PM_When_getPointsForPurchaseTime_Return_zeroPoints() {
        final LocalTime purchaseTime = LocalTime.of(MAX_PURCHASE_TIME, 0);
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForPurchaseTime",
                                                                   purchaseTime);
        assertEquals(ZERO_POINTS, actualPoints);
    }

    @Test
    public void Given_purchaseTimeIs2PM_When_getPointsForPurchaseTime_Return_zeroPoints() {
        final LocalTime purchaseTime = LocalTime.of(MIN_PURCHASE_TIME, 0);
        final Long actualPoints = ReflectionTestUtils.invokeMethod(classUnderTest,
                                                                   "getPointsForPurchaseTime",
                                                                   purchaseTime);
        assertEquals(ZERO_POINTS, actualPoints);
    }
}