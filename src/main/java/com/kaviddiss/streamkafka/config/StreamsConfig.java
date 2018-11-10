package com.kaviddiss.streamkafka.config;

import com.kaviddiss.streamkafka.stream.MasterTransferStreams;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(MasterTransferStreams.class)
public class StreamsConfig {
}
