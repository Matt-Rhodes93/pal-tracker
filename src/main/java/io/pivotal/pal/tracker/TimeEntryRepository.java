package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TimeEntryRepository {

    public TimeEntry create(TimeEntry any);

    public TimeEntry find(long id);

    public List<TimeEntry> list();

    public TimeEntry update(long eq, TimeEntry any);

    public boolean delete(long l);
}
