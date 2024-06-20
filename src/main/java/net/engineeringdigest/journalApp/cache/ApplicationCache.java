package net.engineeringdigest.journalApp.cache;

import net.engineeringdigest.journalApp.entity.JournalEntryAppConfig;
import net.engineeringdigest.journalApp.repository.JournalAppConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationCache {

    public Map<String, String> appCache;

    @Autowired
    private JournalAppConfigEntity journalAppConfigEntity;

    /*
        If a bean is created then the post construct method is called by spring boot
     */
    @PostConstruct
    public void init() {
        /*
            Initializing in init to avoid the possibility of duplicate keys in case some changes the key in the DB
            Instead of adding duplicate keys we re initilaize the appCache collection
         */
        appCache = new HashMap<>();
        List<JournalEntryAppConfig> config = journalAppConfigEntity.findAll();
        for (JournalEntryAppConfig journalEntryAppConfig : config) {
            appCache.put(journalEntryAppConfig.getKey(), journalEntryAppConfig.getValue());
        }
    }
}
