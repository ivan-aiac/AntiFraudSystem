package aiac.antifraudsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Amount_Limit")
public class AmountLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "limits_id")
    private long id;
    @Column(name = "number", nullable = false, unique = true)
    private String cardNumber;
    @Column(name = "allowed", nullable = false)
    private long allowed;
    @Column(name = "manual", nullable = false)
    private long manual;

    public AmountLimit(String cardNumber) {
        this.cardNumber = cardNumber;
        this.allowed = 200;
        this.manual = 1500;
    }

    public AmountLimit() {
    }

    public long getId() {
        return id;
    }

    public long getAllowed() {
        return allowed;
    }

    public long getManual() {
        return manual;
    }

    public void setAllowed(long allowed) {
        this.allowed = allowed;
    }

    public void setManual(long manual) {
        this.manual = manual;
    }
}
