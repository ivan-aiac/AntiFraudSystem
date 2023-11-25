package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.enums.Region;
import aiac.antifraudsystem.enums.TransactionType;
import aiac.antifraudsystem.model.Transaction;
import aiac.antifraudsystem.validation.CreditCard;
import aiac.antifraudsystem.validation.IPv4;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class TransactionDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long transactionId;
    @Positive
    @NotNull
    private long amount;
    @IPv4
    private String ip;
    @CreditCard
    private String number;
    @NotNull
    private Region region;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TransactionType result;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String feedback;

    public TransactionDTO() {}

    public TransactionDTO(long transactionId, long amount, String ip, String number, Region region,
                          LocalDateTime date, TransactionType result, String feedback) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
        this.result = result;
        this.feedback = feedback;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getAmount() {
        return amount;
    }

    public String getIp() {
        return ip;
    }

    public String getNumber() {
        return number;
    }

    public Region getRegion() {
        return region;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getResult() {
        return result;
    }

    public String getFeedback() {
        return feedback;
    }

    public Transaction toTransaction() {
        return new Transaction(amount, ip, number, region, date);
    }

    public static TransactionDTO of(Transaction t) {
        return new TransactionDTO(
                t.getId(),
                t.getAmount(),
                t.getIp(),
                t.getNumber(),
                t.getRegion(),
                t.getDate(),
                t.getType(),
                t.getFeedback().getMessage()
        );
    }
}
