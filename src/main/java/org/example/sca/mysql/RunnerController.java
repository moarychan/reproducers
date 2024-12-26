package org.example.sca.mysql;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runner")
public class RunnerController {

    private final AzureMysqlAuthenticationPluginService authPluginService;

    public RunnerController(AzureMysqlAuthenticationPluginService authPluginService) {
        this.authPluginService = authPluginService;
    }

    @GetMapping("/token/concurrent/less/threads/{sleepInSeconds}/{numberOfRuns}")
    public String tokenConcurrentWithTimesAndLessThreads(@PathVariable("sleepInSeconds") int sleepInSeconds,
                                                         @PathVariable("numberOfRuns") int numberOfRuns) {
        authPluginService.verifyRunner(numberOfRuns, (num) -> authPluginService.useTokenCredentialAndLessThreadsAvailable(sleepInSeconds, num, 0));
        return "Started with " + numberOfRuns + " number of runs and consuming threads using sleep " + sleepInSeconds + " seconds";
    }

    @GetMapping("/token/concurrent/{numberOfRuns}/{tokenTimeoutInSeconds}")
    public String tokenTimeoutConcurrentWithTimes(@PathVariable("numberOfRuns") int numberOfRuns,
                                                  @PathVariable(name = "tokenTimeoutInSeconds", required = false) int tokenTimeoutInSeconds) {
        authPluginService.verifyRunner(numberOfRuns, (num) -> authPluginService.useTokenCredentialThreadWhenLessThreadsAvailable(num, tokenTimeoutInSeconds));
        return "Started with " + numberOfRuns + " number of runs and token timeout in " + tokenTimeoutInSeconds + " seconds";
    }
}
