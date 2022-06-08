package az.unibank.unitech.dto.response;

public class AccountResponse {

    private Integer pin;

    private String currency;

    private Double cash;

    public Integer getPin() {
        return pin;
    }

    public AccountResponse setPin(Integer pin) {
        this.pin = pin;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public AccountResponse setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Double getCash() {
        return cash;
    }

    public AccountResponse setCash(Double cash) {
        this.cash = cash;
        return this;
    }
}
