package aiac.antifraudsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private long id;
    @Column(name = "message", nullable = false)
    private String message;
    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public Feedback() {
    }

    public Feedback(Transaction transaction) {
        this.transaction = transaction;
        this.message = "";
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
