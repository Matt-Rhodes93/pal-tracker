package io.pivotal.pal.tracker;


import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping(path = "/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repository;
    private CounterService counterService;
    private GaugeService gaugeService;

    public TimeEntryController(TimeEntryRepository repository, CounterService counterService, GaugeService gaugeService) {
        this.repository = repository;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        counterService.increment("TimeEntry create");
        gaugeService.submit("timeEntries count", repository.list().size());
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.create(timeEntryToCreate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        counterService.increment("TimeEntry read");
        HttpStatus status = HttpStatus.OK;
        TimeEntry entry = repository.find(id);

        if (entry == null) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(entry);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        counterService.increment("TimeEntry list");
        return ResponseEntity.status(HttpStatus.OK).body(repository.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry updateEntry) {
        counterService.increment("TimeEntry update");
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
        counterService.increment("TimeEntry delete");
        gaugeService.submit("timeEntries count", repository.list().size());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleted);
    }
}
