package de.ostfale.beezle.control.repo

import javafx.concurrent.Task

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Clone project from repository
 * Created by usauerbrei on 12.01.2018
 */
class CloneTask {

    static Future cloneProjectFromRepository(String repoName, String localRepoPath) {
        Task worker = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                CloneRepoService repoService = new CloneRepoService()
                repoService.cloneRepository(localRepoPath, repoName)
                return true
            }
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(2)
        return threadPool.submit(worker)
    }

    /*  private ExecutorService executorService = Executors.newSingleThreadExecutor();

      Future<Boolean> cloneProjectFromRepository(String repoName, String localRepoPath) {

          Future<Boolean> future = executorService.submit(new Runnable() {
              void run() {
                  CloneRepoService repoService = new CloneRepoService()
                  repoService.cloneRepository(localRepoPath, repoName)
              }
          })

         return future //returns null if the task has finished correctly.

         *//* return executorService.submit(new Runnable() {
              @Override
              void run() {
                  CloneRepoService repoService = new CloneRepoService()
                  repoService.cloneRepository(localRepoPath, repoName)
              }
          })
          *//*/* return executorService.submit(() -> {
               CloneRepoService repoService = new CloneRepoService()
               repoService.cloneRepository(localRepoPath, repoName)
               return true
           })*/

}
