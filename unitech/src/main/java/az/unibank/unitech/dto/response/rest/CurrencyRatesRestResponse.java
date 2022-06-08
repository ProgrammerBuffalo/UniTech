package az.unibank.unitech.dto.response.rest;

public class CurrencyRatesRestResponse {

    private String from;

    private String to;

    private Double rate;

    public String getFrom() {
        return from;
    }

    public CurrencyRatesRestResponse setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public CurrencyRatesRestResponse setTo(String to) {
        this.to = to;
        return this;
    }

    public Double getRate() {
        return rate;
    }

    public CurrencyRatesRestResponse setRate(Double rate) {
        this.rate = rate;
        return this;
    }
}
