package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.model.StolenCard;
import aiac.antifraudsystem.validation.CreditCard;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StolenCardDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @CreditCard
    private String number;

    public StolenCardDTO() {
    }

    public StolenCardDTO(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public StolenCard toStolenCard() {
        return new StolenCard(number);
    }

    public static StolenCardDTO of(StolenCard stolenCard) {
        return new StolenCardDTO(stolenCard.getId(), stolenCard.getNumber());
    }
}
