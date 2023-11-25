package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.model.AppUser;
import aiac.antifraudsystem.model.AppUserDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;

    public UserDTO(long id, String name, String username, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = null;
        this.role = role;
    }

    public UserDTO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public AppUser mapToAppUser() {
        return new AppUser(name, username, password);
    }

    public static UserDTO of(AppUser user) {
        AppUserDetails userDetails = user.getUserDetails();
        return new UserDTO(user.getId(), user.getName(), userDetails.getUsername(), userDetails.getRole().name());
    }
}
