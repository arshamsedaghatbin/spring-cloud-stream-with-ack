package com.kaviddiss.streamkafka.service;

import com.kaviddiss.streamkafka.model.*;
import com.kaviddiss.streamkafka.repository.MasterTransferRepository;
import com.kaviddiss.streamkafka.stream.MasterTransferStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class TransferListener {

    @Autowired
    private MasterTransferRepository masterTransferRepository;

    @Autowired
    TransferServiceImpl transferService;

    @StreamListener(MasterTransferStreams.INPUT)
    public void handleGreetings(@Payload MasterTransferDTO masterTransfer, @Header("kafka_acknowledgment") Acknowledgment acknowledgment) throws Exception {
        Integer transferIndex = getTransferIndex(masterTransfer);
        if (transferIndex != -1) {
            executeTransfer(transferIndex, masterTransfer);
            transferService.sendTransfer(masterTransfer);
            acknowledgment.acknowledge();
        } else {
            acknowledgment.acknowledge();
            MasterTransferDTO byMasterTransferId = masterTransferRepository.
                    findByMasterTransferId(masterTransfer.getMasterTransferId()).orElseThrow(() -> new Exception());
        }

        System.out.println();
//        }
    }

    private void executeTransfer(Integer transferIndex, MasterTransferDTO masterTransfer) {
        TransferStatus nextTransferStatus = getNextTransferStatus(transferIndex, masterTransfer);
        Transfer transfer = getTransferByIndex(transferIndex, masterTransfer);
        Boolean permissionToCall = followUp(transferIndex, masterTransfer, nextTransferStatus);
        switch (transfer.getGateway()) {
            case BABEL:
                if (permissionToCall) {
                    changeTransferState(transferIndex, masterTransfer, nextTransferStatus);
                    if (nextTransferStatus.equals(TransferStatus.PENDING_REVERSED)) {
//                        generate reversed transfer
                        changeTransferState(transferIndex, masterTransfer, TransferStatus.REVERSED);
                    }
                    reversedFailed(transferIndex, masterTransfer);
                    changeTransferState(transferIndex, masterTransfer, TransferStatus.SUCCESS);
                    increaseTransferIndex(masterTransfer);
//                    }
                }
                break;
            case KD:
                if (permissionToCall) {
                    changeTransferState(transferIndex, masterTransfer, nextTransferStatus);
                    if (nextTransferStatus.equals(TransferStatus.PENDING_REVERSED)) {
//                        generate reversed transfer
                        changeTransferState(transferIndex, masterTransfer, TransferStatus.REVERSED);
                    }
//                  call rest
                    else {
                        changeTransferState(transferIndex, masterTransfer, TransferStatus.SUCCESS);
                        increaseTransferIndex(masterTransfer);
                    }
                }

                break;
        }


    }

    private void reversedFailed(Integer transferIndex, MasterTransferDTO masterTransfer) {

        Transfer transfer = getTransferByIndex(transferIndex, masterTransfer);
        if (transfer.getRetryReverseFailedCounter() == 3) {
            masterTransfer.setMasterTransferStatus(MasterTransferStatus.NOT_FINISHED);
        }
        transfer.setRetryReverseFailedCounter(transfer.getRetryReverseFailedCounter() + 1);
    }

    private void increaseTransferIndex(MasterTransferDTO masterTransfer) {
        masterTransfer.setSuccessTransferIndex(masterTransfer.getSuccessTransferIndex() + 1);
    }

    private TransferStatus getNextTransferStatus(Integer transferIndex, MasterTransferDTO masterTransfer) {
        Transfer transfer = getTransferByIndex(transferIndex, masterTransfer);
        if (transfer.getTransferStatus().equals(TransferAction.reversed.getFrom())) {
            return TransferAction.reversed.getTo();
        } else if (transfer.getTransferStatus().equals(TransferAction.retry_reserve.getFrom())) {
            return TransferAction.retry_reserve.getTo();
        } else if (masterTransfer.getMasterTransferStatus().equals(MasterTransferStatus.FAILED)) {
            return TransferAction.pending_reversed.getTo();
        } else return TransferStatus.PENDING;

    }

    private Boolean followUp(Integer transferIndex, MasterTransferDTO masterTransfer, TransferStatus nextTransferStatus) {
        Optional<MasterTransferDTO> masterTransferDBOptional =
                masterTransferRepository.findByMasterTransferId(masterTransfer.getMasterTransferId());
        if (!masterTransferDBOptional.isPresent()) {
            return true;
        }
        MasterTransferDTO masterTransferDB = masterTransferDBOptional.get();
        Transfer transfer = getTransferByIndex(transferIndex, masterTransferDB);
        if (repeatedTransaction(transfer, nextTransferStatus)) {
            setTransferByIndex(transferIndex, masterTransfer, transfer);
            increaseTransferIndex(masterTransfer);
            return false;
        } else if (needToFollowup(transfer)) {
//                  call followup  if true change permissionToCall false and update transfer state
            changeTransferState(transferIndex, masterTransfer, getNextTransferStatus(transferIndex, masterTransfer));
            return false;
        }
        return true;
    }

    private boolean repeatedTransaction(Transfer transfer, TransferStatus nextTransferStatus) {
        if (isRepeatCondition(transfer, nextTransferStatus)) {
            return true;
        } else return false;
    }


    private void changeTransferState(Integer transferIndex,
                                     MasterTransferDTO masterTransfer,
                                     TransferStatus transferStatus) {
        if (transferStatus.equals(TransferStatus.SUCCESS) ||
                transferStatus.equals(TransferStatus.FAILED) ||
                transferStatus.equals(TransferStatus.REVERSED)) {
            masterTransfer.getExecutedTransfers().add(getTransferByIndex(transferIndex, masterTransfer));
        }
        getTransferByIndex(transferIndex, masterTransfer).
                setTransferStatus(transferStatus);
        masterTransferRepository.save(masterTransfer);
    }

    private Transfer getTransferByIndex(Integer transferIndex, MasterTransferDTO masterTransfer) {
        return masterTransfer.getTransfers().
                get(transferIndex);
    }

    private void setTransferByIndex(Integer transferIndex, MasterTransferDTO masterTransfer, Transfer transfer) {
        // change status instead of remove
        masterTransfer.getTransfers().
                remove(transferIndex.intValue());
        masterTransfer.getTransfers().
                add(transferIndex.intValue(), transfer);
    }

    private Boolean needToFollowup(Transfer transfer) {
        if (!transfer.getTransferStatus().equals(TransferStatus.PENDING) &&
                !transfer.getTransferStatus().equals(TransferStatus.PENDING_REVERSED)) {
            return false;
        } else return true;

    }


    private Integer getTransferIndex(MasterTransferDTO masterTransfer) {

        if (masterTransfer.getMasterTransferStatus().equals(MasterTransferStatus.FAILED)) {
            if (!getTransferByIndex(0, masterTransfer).getTransferStatus().equals(TransferStatus.REVERSED)) {
                return getNextReverseIndex(masterTransfer);
            }
            return -1;
        } else if (masterTransfer.getTransfers().get(masterTransfer.getTransfers().size() - 1).getTransferStatus()
                .equals(TransferStatus.SUCCESS)) {
            masterTransfer.setMasterTransferStatus(MasterTransferStatus.SUCCESS);
            masterTransferRepository.save(masterTransfer);
            return -1;
        } else {
            return masterTransfer.getSuccessTransferIndex();
        }

    }

    private Integer getNextReverseIndex(MasterTransferDTO masterTransfer) {
        for (Integer index = 0; masterTransfer.getSuccessTransferIndex() >= index; index++) {
            if (getTransferByIndex(index, masterTransfer).getTransferStatus().equals(TransferStatus.REVERSED)) {
                return index - 1;
            } else if (getTransferByIndex(index, masterTransfer).getTransferStatus().equals(TransferStatus.FAILED)) {
                return index - 1;
            }
        }
        return -1;
    }

    private boolean isRepeatCondition(Transfer transfer, TransferStatus nextTransferStatus) {
        return (transfer.getTransferStatus().equals(TransferStatus.SUCCESS) &&
                !nextTransferStatus.equals(TransferStatus.PENDING_REVERSED))
                || transfer.getTransferStatus().equals(TransferStatus.FAILED)
                || transfer.getTransferStatus().equals(TransferStatus.REVERSED);
    }

}
