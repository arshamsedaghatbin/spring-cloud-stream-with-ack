package com.kaviddiss.streamkafka.service;

import com.kaviddiss.streamkafka.model.MasterTransferDTO;

public interface TransferService {
    void sendTransfer(MasterTransferDTO masterTransfer);
}
