package com.kaviddiss.streamkafka.model;

public enum TransferAction {
    reversed(TransferStatus.PENDING_REVERSED, TransferStatus.REVERSED),
    retry_reserve(TransferStatus.FAILED_REVERSED, TransferStatus.PENDING_REVERSED),
    pending_reversed(TransferStatus.FAILED, TransferStatus.PENDING_REVERSED),
    notStarted(TransferStatus.NOT_START, TransferStatus.PENDING);


    private final TransferStatus from;
    private final TransferStatus to;

    private TransferAction(TransferStatus from, TransferStatus to) {
        this.from = from;
        this.to = to;
    }

    public TransferStatus getFrom() {
        return this.from;
    }

    public TransferStatus getTo() {
        return this.to;
    }
}
