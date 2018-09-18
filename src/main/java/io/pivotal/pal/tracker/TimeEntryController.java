package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.create(timeEntryToCreate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        HttpStatus status = HttpStatus.OK;
        TimeEntry entry = repository.find(id);

        if (entry == null) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(entry);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(repository.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry updateEntry) {
        HttpStatus status = HttpStatus.OK;
        TimeEntry entry = repository.update(id, updateEntry);

        if (entry == null) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(entry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable  long id) {
        Boolean deleted = repository.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleted);
    }
}
