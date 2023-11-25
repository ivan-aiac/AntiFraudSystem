package aiac.antifraudsystem.dto;

public class DeleteUserResponse {
    private String username;
    private String status;

    public DeleteUserResponse(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public DeleteUserResponse() {}

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }
}
