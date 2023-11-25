package aiac.antifraudsystem.dto;

public class SimpleResponse {

    private String status;

    public SimpleResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
