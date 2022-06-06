package az.unibank.unitech.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer pin;

    @Column
    @ColumnDefault("true")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Account() {
    }

    public Integer getId() {
        return id;
    }

    public Account setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPin() {
        return pin;
    }

    public Account setPin(Integer pin) {
        this.pin = pin;
        return this;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Account setActive(Boolean active) {
        isActive = active;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Account setUser(User user) {
        this.user = user;
        return this;
    }
}
