package com.kaviddiss.streamkafka.service;

import com.kaviddiss.streamkafka.model.MasterTransferDTO;
import com.kaviddiss.streamkafka.stream.MasterTransferStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
@Slf4j
public class TransferServiceImpl implements TransferService{
    private final MasterTransferStreams transferStreams;

    public TransferServiceImpl(MasterTransferStreams greetingsStreams) {
        this.transferStreams = greetingsStreams;
    }

    public void sendTransfer(final MasterTransferDTO greetings) {

        MessageChannel messageChannel = transferStreams.outboundGreetings();
        messageChannel.send(MessageBuilder
                .withPayload(greetings)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

}
