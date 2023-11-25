package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.enums.AppRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RoleChangeRequest {
    @NotBlank
    private String username;
    @NotNull
    private AppRole role;
    public RoleChangeRequest() {}

    public String getUsername() {
        return username;
    }
    public AppRole getRole() {
        return role;
    }
}
