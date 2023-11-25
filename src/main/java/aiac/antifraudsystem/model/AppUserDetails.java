package aiac.antifraudsystem.model;

import aiac.antifraudsystem.enums.AppRole;
import jakarta.persistence.*;

@Entity
@Table(name = "User_Details")
public class AppUserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "details_id")
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppRole role;
    @Column(name = "unlocked", nullable = false)
    private boolean nonLocked;

    public AppUserDetails() {
    }

    public AppUserDetails(AppUser user, String username, String password) {
        this.user = user;
        this.username = username;
        this.password = password;
        nonLocked = false;
        role = AppRole.MERCHANT;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AppRole getRole() {
        return role;
    }

    public void setRole(AppRole role) {
        this.role = role;
    }

    public boolean isNonLocked() {
        return nonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        this.nonLocked = nonLocked;
    }
}
