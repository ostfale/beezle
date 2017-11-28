package de.ostfale.beezle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CloneTask {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Future<Boolean> cloneRepository() {
        return executorService.submit(() -> {

            return true;
        });
    }
}
