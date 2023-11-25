package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FeedbackRequest {
    @Positive
    @NotNull
    private long transactionId;
    @NotNull
    private TransactionType feedback;

    private FeedbackRequest() {

    }

    public long getTransactionId() {
        return transactionId;
    }

    public TransactionType getFeedback() {
        return feedback;
    }
}
