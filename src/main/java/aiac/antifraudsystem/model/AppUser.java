package aiac.antifraudsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    @Column(name = "name", length = 50)
    private String name;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AppUserDetails userDetails;

    public AppUser() {}

    public AppUser(String name, String username, String password) {
        this.name = name;
        userDetails = new AppUserDetails(this, username, password);
    }

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

    public AppUserDetails getUserDetails() {
        return userDetails;
    }
}
