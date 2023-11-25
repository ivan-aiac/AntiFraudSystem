package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.enums.TransactionType;

public class TransactionResponse {

    private TransactionType result;
    private String info;

    public TransactionResponse() {
    }

    public TransactionResponse(TransactionType result, String info) {
        this.result = result;
        this.info = info;
    }

    public TransactionType getResult() {
        return result;
    }

    public String getInfo() {
        return info;
    }
}
