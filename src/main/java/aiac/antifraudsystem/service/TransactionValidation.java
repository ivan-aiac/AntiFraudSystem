package aiac.antifraudsystem.service;

import aiac.antifraudsystem.enums.TransactionType;

public record TransactionValidation(TransactionType result, String rejections) {
}
