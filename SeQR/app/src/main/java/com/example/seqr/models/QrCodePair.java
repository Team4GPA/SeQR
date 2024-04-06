package com.example.seqr.models;

public class QrCodePair {
    private String checkInQR;
    private String promotionQR;
    private String previousEventName;

    public QrCodePair(String checkInQR, String promotionQR, String previousEventName){
        this.checkInQR = checkInQR;
        this.promotionQR = promotionQR;
        this.previousEventName = previousEventName;
    }

    public String getCheckInQR() {
        return checkInQR;
    }

    public void setCheckInQR(String checkInQR) {
        this.checkInQR = checkInQR;
    }

    public String getPromotionQR() {
        return promotionQR;
    }

    public void setPromotionQR(String promotionQR) {
        this.promotionQR = promotionQR;
    }

    public String getPreviousEventName() {
        return previousEventName;
    }

    public void setPreviousEventName(String previousEventName) {
        this.previousEventName = previousEventName;
    }
}
