package aiac.antifraudsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Stolen_Card")
public class StolenCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stolen_card_id")
    private long id;
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    public StolenCard(String number) {
        this.number = number;
    }

    public StolenCard() {}

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
