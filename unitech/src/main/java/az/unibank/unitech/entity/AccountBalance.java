package az.unibank.unitech.entity;

import javax.persistence.*;

@Entity
@Table(name = "accounts_balance")
public class AccountBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Double cash;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Currency currency;

    @JoinColumn(nullable = false, referencedColumnName = "id")
    @OneToOne
    private Account account;

    public AccountBalance() {
    }

    public Integer getId() {
        return id;
    }

    public AccountBalance setId(Integer id) {
        this.id = id;
        return this;
    }

    public Double getCash() {
        return cash;
    }

    public AccountBalance setCash(Double cash) {
        this.cash = cash;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountBalance setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public AccountBalance setAccount(Account account) {
        this.account = account;
        return this;
    }
}
