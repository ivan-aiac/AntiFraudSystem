package aiac.antifraudsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Suspicious_Ip")
public class SuspiciousIp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suspicious_ip_id")
    private long id;
    @Column(name = "ip", nullable = false, unique = true)
    private String ip;

    public SuspiciousIp(String ip) {
        this.ip = ip;
    }

    public SuspiciousIp(){}

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }
}
