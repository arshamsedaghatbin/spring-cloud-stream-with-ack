package com.kaviddiss.streamkafka.model;

public class Transfer {

    private Gateway gateway;
    private TransferStatus transferStatus;
    private String transferId;
    private int retryReverseFailedCounter;


    public int getRetryReverseFailedCounter() {
        return retryReverseFailedCounter;
    }

    public void setRetryReverseFailedCounter(int retryReverseFailedCounter) {
        this.retryReverseFailedCounter = retryReverseFailedCounter;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }
}
