package de.ostfale.beezle;

import de.ostfale.beezle.control.PropertyService;
import de.ostfale.beezle.control.UserProperties;
import de.ostfale.beezle.control.repo.CloneRepoService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CloneTask {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Future<Boolean> cloneRepository(String repoName) {
        return executorService.submit(() -> {
            String localRepoPath = PropertyService.instance.getProperty(UserProperties.REPO_PATH.getKey());
            CloneRepoService repoService = new CloneRepoService();
            repoService.cloneRepository(localRepoPath, repoName);
            return true;
        });
    }
}
