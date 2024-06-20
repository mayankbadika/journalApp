package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
class JournalEntryController {

    /*
        Methods inside a controller class should be public,
        so that they can be accessed and invoked by the spring framework or external http request
    */

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournals() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        List<JournalEntry> all = user.getJournalEntries();

        if(all !=null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            entry.setDate(LocalDateTime.now());
            boolean success = journalEntryService.saveNewJournalEntry(entry, username);
            if(success) {
                return new ResponseEntity<>(entry, HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);
        Optional<JournalEntry> journalEntry = user.getJournalEntries()
                .stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntryByID(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean deleted = journalEntryService.deleteByID(id, username);
        if(deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username);
        Optional<JournalEntry> journal = user.getJournalEntries().stream().filter( x-> x.getId().equals(id)).findFirst();
        if(journal.isPresent()) {
            JournalEntry old = journal.get();
            old.setTitle(entry.getTitle()!=null && entry.getTitle()!="" ? entry.getTitle() : old.getTitle());
            old.setContent(entry.getContent()!=null && entry.getContent()!="" ? entry.getContent() : old.getContent());
            journalEntryService.updateJournalEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }

       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
