package aiac.antifraudsystem.dto;

import aiac.antifraudsystem.model.SuspiciousIp;
import aiac.antifraudsystem.validation.IPv4;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SusIpDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    @IPv4
    private String ip;

    public SusIpDTO(long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public SusIpDTO() {
    }

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public SuspiciousIp toSuspiciousIp() {
        return new SuspiciousIp(ip);
    }

    public static SusIpDTO of(SuspiciousIp suspiciousIp) {
        return new SusIpDTO(suspiciousIp.getId(), suspiciousIp.getIp());
    }
}
