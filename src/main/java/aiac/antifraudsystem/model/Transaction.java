package aiac.antifraudsystem.model;

import aiac.antifraudsystem.enums.Region;
import aiac.antifraudsystem.enums.TransactionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long id;
    @Column(name = "amount", nullable = false)
    private long amount;
    @Column(name = "ip", nullable = false)
    private String ip;
    @Column(name = "number", nullable = false)
    private String number;
    @Column(name = "region", nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;
    @Column(name = "date", nullable = false)
    private LocalDateTime date;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Feedback feedback;

    public Transaction() {}

    public Transaction(long amount, String ip, String number, Region region, LocalDateTime date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
        this.feedback = new Feedback(this);
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public String getIp() {
        return ip;
    }

    public String getNumber() {
        return number;
    }

    public Region getRegion() {
        return region;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public Feedback getFeedback() {
        return feedback;
    }

}
