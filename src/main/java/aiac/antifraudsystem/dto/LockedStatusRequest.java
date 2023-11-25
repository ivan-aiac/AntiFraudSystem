package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.enums.LockOperation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LockedStatusRequest {
    @NotBlank
    private String username;
    @NotNull
    private LockOperation operation;

    public LockedStatusRequest() {}

    public String getUsername() {
        return username;
    }

    public LockOperation getOperation() {
        return operation;
    }
}
