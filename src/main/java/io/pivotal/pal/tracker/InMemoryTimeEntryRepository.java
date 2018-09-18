package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private List<TimeEntry> entries = new ArrayList<>();
    private long countId = 0L;

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        delete(id);
        timeEntry.setId(id);
        entries.add(timeEntry);
        return timeEntry;
    }

    @Override
    public boolean delete(long id) {
        TimeEntry entry = this.find(id);
        if(entry != null) {
            return entries.remove(entry);
        }
        return false;
    }


    @Override
    public TimeEntry find(long id) {
               return entries.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<TimeEntry>(entries);
    }

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(++countId);

        entries.add(timeEntry);
        return timeEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayList)) return false;
        List<TimeEntry> that = (List<TimeEntry>) o;
        if(entries.size() != that.size())
            return false;

        return entries.stream()
                .allMatch(t -> that.contains(t));
    }

    @Override
    public int hashCode() {
        return Objects.hash(entries, countId);
    }
}
