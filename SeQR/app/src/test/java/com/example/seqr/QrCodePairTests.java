package com.example.seqr;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertNull;

import com.example.seqr.models.QrCodePair;

import org.junit.Test;

public class QrCodePairTests {

    @Test
    public void testSettersGetters(){
        QrCodePair mockQrCodePair = new QrCodePair();
        mockQrCodePair.setCheckInQR("Test CheckInQR");
        mockQrCodePair.setPromotionQR("Test promotionQR");
        mockQrCodePair.setEventId("Test eventID");
        mockQrCodePair.setPreviousEventName("Test eventName");
        assertEquals("Test promotionQR",mockQrCodePair.getPromotionQR());
        assertEquals("Test eventID",mockQrCodePair.getEventId());
        assertEquals("Test eventName",mockQrCodePair.getPreviousEventName());
        assertEquals("Test CheckInQR",mockQrCodePair.getCheckInQR());

    }

    @Test
    public void testConstructor(){
        QrCodePair mockQrCodePair = new QrCodePair("Test CheckInQR", "Test promotionQR", "Test eventName");
        assertEquals("Test promotionQR",mockQrCodePair.getPromotionQR());
        assertEquals("Test eventName",mockQrCodePair.getPreviousEventName());
        assertEquals("Test CheckInQR",mockQrCodePair.getCheckInQR());

    }

    @Test
    public void testEmptyConstructor(){
        QrCodePair MockQrCodePair = new QrCodePair();

        assertNull(MockQrCodePair.getCheckInQR());
        assertNull(MockQrCodePair.getPromotionQR());
        assertNull(MockQrCodePair.getEventId());
        assertNull(MockQrCodePair.getPreviousEventName());
    }
}
