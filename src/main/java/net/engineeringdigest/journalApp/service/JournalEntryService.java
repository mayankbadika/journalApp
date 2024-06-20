package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    JournalEntryRepository journalEntryRepository;

    @Autowired
    UserService userService;

    @Transactional
    public boolean saveNewJournalEntry(JournalEntry entry, String username) {
        try {
            User user = userService.findByUsername(username);
            if(user != null){
                JournalEntry saved = journalEntryRepository.save(entry);

                user.getJournalEntries().add(entry);
                userService.saveUser(user);
                return true;
            }
        } catch(Exception e) {
            throw new RuntimeException("Excpetion occured during transaction, rolling back", e);
        }

        return false;
    }

    public void updateJournalEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findByID(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteByID(ObjectId id, String username) {
        boolean removed = false;
        try {
            User user = userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch(Exception e) {
            throw new RuntimeException("An error occurred while deleting the entry", e);
        }

        return removed;
    }
}
