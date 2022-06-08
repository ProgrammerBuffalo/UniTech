package az.unibank.unitech.dto.response;

public class BalanceTransferResponse {

    private Integer senderPin;

    private Double leftCashOfSender;

    private Integer receiverPin;

    private Double newCashOfReceiver;

    public Integer getSenderPin() {
        return senderPin;
    }

    public BalanceTransferResponse setSenderPin(Integer senderPin) {
        this.senderPin = senderPin;
        return this;
    }

    public Double getLeftCashOfSender() {
        return leftCashOfSender;
    }

    public BalanceTransferResponse setLeftCashOfSender(Double leftCashOfSender) {
        this.leftCashOfSender = leftCashOfSender;
        return this;
    }

    public Integer getReceiverPin() {
        return receiverPin;
    }

    public BalanceTransferResponse setReceiverPin(Integer receiverPin) {
        this.receiverPin = receiverPin;
        return this;
    }

    public Double getNewCashOfReceiver() {
        return newCashOfReceiver;
    }

    public BalanceTransferResponse setNewCashOfReceiver(Double newCashOfReceiver) {
        this.newCashOfReceiver = newCashOfReceiver;
        return this;
    }
}
