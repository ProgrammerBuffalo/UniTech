package az.unibank.unitech.entity;

import javax.persistence.*;

@Entity
@Table(name = "exchange_rates", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "from_currency_id", "to_currency_id" })
})
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "from_currency_id", nullable = false)
    @ManyToOne
    private Currency from;

    @JoinColumn(name = "to_currency_id", nullable = false)
    @ManyToOne
    private Currency to;

    @Column
    private Double rate;

    public ExchangeRate() {
    }

    public Integer getId() {
        return id;
    }

    public ExchangeRate setId(Integer id) {
        this.id = id;
        return this;
    }

    public Currency getFrom() {
        return from;
    }

    public ExchangeRate setFrom(Currency from) {
        this.from = from;
        return this;
    }

    public Currency getTo() {
        return to;
    }

    public ExchangeRate setTo(Currency to) {
        this.to = to;
        return this;
    }

    public Double getRate() {
        return rate;
    }

    public ExchangeRate setRate(Double rate) {
        this.rate = rate;
        return this;
    }
}
