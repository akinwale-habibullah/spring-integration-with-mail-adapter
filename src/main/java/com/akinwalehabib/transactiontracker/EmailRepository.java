package com.akinwalehabib.transactiontracker;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String>{}
