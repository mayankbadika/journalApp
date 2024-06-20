package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntryAppConfig;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalAppConfigEntity extends MongoRepository<JournalEntryAppConfig, ObjectId> { }
