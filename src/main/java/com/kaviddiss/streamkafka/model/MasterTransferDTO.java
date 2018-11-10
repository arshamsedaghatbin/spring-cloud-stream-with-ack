package com.kaviddiss.streamkafka.model;

// lombok autogenerates getters, setters, toString() and a builder (see https://projectlombok.org/):

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MasterTransferDTO {
    @Id
    private String id;
    private String masterTransferId;
    private LinkedList<Transfer> transfers;
    private MasterTransferStatus masterTransferStatus;
    private List<Transfer> executedTransfers;
    private Integer successTransferIndex =0;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSuccessTransferIndex() {
        return successTransferIndex;
    }

    public void setSuccessTransferIndex(Integer successTransferIndex) {
        this.successTransferIndex = successTransferIndex;
    }



    public List<Transfer> getExecutedTransfers() {
        if(executedTransfers ==null){
            executedTransfers =new ArrayList<>();
        }
        return executedTransfers;
    }

    public void setExecutedTransfers(List<Transfer> executedTransfers) {
        this.executedTransfers = executedTransfers;
    }

    public LinkedList<Transfer> getTransfers() {
        return transfers;
    }

    public void setTransfers(LinkedList<Transfer> transfers) {
        this.transfers = transfers;
    }

    public String getMasterTransferId() {
        return masterTransferId;
    }

    public void setMasterTransferId(String masterTransferId) {
        this.masterTransferId = masterTransferId;
    }

    public MasterTransferStatus getMasterTransferStatus() {
        return masterTransferStatus;
    }

    public void setMasterTransferStatus(MasterTransferStatus masterTransferStatus) {
        this.masterTransferStatus = masterTransferStatus;
    }
}
