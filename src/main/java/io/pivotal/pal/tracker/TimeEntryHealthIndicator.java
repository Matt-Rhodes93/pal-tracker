package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {

    @Autowired
    private TimeEntryRepository repository;

    @Override
    public Health health() {
        Status healthStatus = Status.DOWN;

        if (repository.list().size() < 5) {
            healthStatus = Status.UP;
        }


        Health health = new Health.Builder(healthStatus).build();
        return health;
    }
}
