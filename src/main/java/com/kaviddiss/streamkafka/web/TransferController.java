package com.kaviddiss.streamkafka.web;

import com.kaviddiss.streamkafka.model.*;
import com.kaviddiss.streamkafka.repository.MasterTransferRepository;
import com.kaviddiss.streamkafka.service.TransferService;
import com.kaviddiss.streamkafka.service.TransferServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.UUID;

@RestController
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService greetingsService) {
        this.transferService = greetingsService;
    }


    @Autowired
    private MasterTransferRepository masterTransferRepository;
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void greetings() {
        masterTransferRepository.findAll();
        MasterTransferDTO masterTransfer = getMasterTransfer();
        transferService.sendTransfer(masterTransfer);
    }

    private MasterTransferDTO getMasterTransfer() {
        MasterTransferDTO masterTransfer =new MasterTransferDTO();
        masterTransfer.setMasterTransferId(UUID.randomUUID().toString());
        masterTransfer.setMasterTransferStatus(MasterTransferStatus.STARTED);
        LinkedList<Transfer> transfers=new LinkedList<>();
        Transfer transfer=new Transfer();
        transfer.setGateway(Gateway.BABEL);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);

        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);


        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);


        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);


        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);


        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);


        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);

        transfer=new Transfer();
        transfer.setGateway(Gateway.KD);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        masterTransfer.setTransfers(transfers);


         transfer=new Transfer();
        transfer.setGateway(Gateway.BABEL);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);

         transfer=new Transfer();
        transfer.setGateway(Gateway.BABEL);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);

         transfer=new Transfer();
        transfer.setGateway(Gateway.BABEL);
        transfer.setTransferId(UUID.randomUUID().toString());
        transfer.setTransferStatus(TransferStatus.NOT_START);
        transfers.add(transfer);
        return masterTransfer;
    }
}
