package aiac.antifraudsystem.enums;

public enum Rejections {
    SUS_IP("ip"),
    STOLEN_CARD("card-number"),
    HIGH_AMOUNT("amount"),
    MULTIPLE_IP("ip-correlation"),
    MULTIPLE_REGION("region-correlation"),
    NONE("none");

    private final String reason;

    Rejections(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
