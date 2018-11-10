package com.kaviddiss.streamkafka.repository;

import com.kaviddiss.streamkafka.model.MasterTransferDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MasterTransferRepository extends MongoRepository<MasterTransferDTO, String> {

    Optional<MasterTransferDTO> findByMasterTransferId(String id);
//      MasterTransfer findByMasterTransferId(String id);

}