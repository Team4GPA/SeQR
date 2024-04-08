package com.example.seqr.models;

/**
 * Class to represent the checkInQR and promotionQR of a deleted event
 */
public class QrCodePair {
    private String checkInQR;
    private String promotionQR;
    private String previousEventName;
    private String eventId;

    /**
     * Empty constructor
     */
    public QrCodePair(){

    }

    /**
     * Constructor method to make a new Qr Code pair
     * @param checkInQR raw data of checkInQR
     * @param promotionQR raw data of promotional qr
     * @param previousEventName name of the event these were used for
     */
    public QrCodePair(String checkInQR, String promotionQR, String previousEventName){
        this.checkInQR = checkInQR;
        this.promotionQR = promotionQR;
        this.previousEventName = previousEventName;
    }


    /**
     * Retrieves the QR code for check-in.
     *
     * @return The QR code for check-in.
     */
    public String getCheckInQR() {
        return checkInQR;
    }

    /**
     * Sets the QR code for check-in.
     *
     * @param checkInQR The QR code for check-in.
     */
    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    /**
     * Retrieves the QR code for promotion.
     *
     * @return The QR code for promotion.
     */
    public String getPromotionQR() {
        return promotionQR;
    }

    /**
     * Sets the QR code for promotion.
     *
     * @param promotionQR The QR code for promotion.
     */
    public void setPromotionQR(String promotionQR) {
        this.promotionQR = promotionQR;
    }

    /**
     * Retrieves the name of the previous event.
     *
     * @return The name of the previous event.
     */
    public String getPreviousEventName() {
        return previousEventName;
    }

    /**
     * Sets the name of the previous event.
     *
     * @param previousEventName The name of the previous event.
     */
    public void setPreviousEventName(String previousEventName) {
        this.previousEventName = previousEventName;
    }

    /**
     * Retrieves the ID of the event associated with the QR pair.
     *
     * @return The ID of the event associated with the QR pair.
     */
    public String getEventId() {
        return eventId;
    }


    /**
     * Sets the ID of the event associated with the QR pair.
     *
     * @param eventId The ID of the event associated with the QR pair.
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
