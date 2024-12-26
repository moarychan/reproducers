package org.example.sca.mysql;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

@Component
public class AzureMysqlAuthenticationPluginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureMysqlAuthenticationPluginService.class);

    private final TokenCredential tokenCredential;

    public AzureMysqlAuthenticationPluginService(@Qualifier("test") TokenCredential tokenCredential) {
        this.tokenCredential = tokenCredential;
    }

    public void verifyRunner(int times, IntConsumer intConsumer) {
        IntStream.range(0, times)
                 .forEach(intConsumer);
    }


    public void useTokenCredentialAndLessThreadsAvailable(int sleepInSeconds, int number, int tokenTimeoutInSeconds) {
        consumeForkJoinPoolThread(sleepInSeconds, number);
        useTokenCredentialThreadWhenLessThreadsAvailable(number, tokenTimeoutInSeconds);
    }

    public void consumeForkJoinPoolThread(int sleepInSeconds, int number) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.execute(() -> {
            LOGGER.info("[Consuming fork join pool ] Start......... -> {}", number);
            IntStream.range(0, sleepInSeconds)
                .forEach(ignore -> sleepInSeconds(1));
            LOGGER.info("[Consuming fork join pool ] End......... -> {}", number);
        });
    }

    private void sleepInSeconds(int sleepInSeconds) {
        LOGGER.info("Sleeping in {} second{}...", sleepInSeconds, sleepInSeconds > 1 ? "s" : "");
        try {
            TimeUnit.SECONDS.sleep(sleepInSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void useTokenCredentialThreadWhenLessThreadsAvailable(int number, int tokenTimeoutInSeconds) {
        Thread acquireTokenThread = new Thread(() -> {
            LOGGER.info("[TokenCredential less thread] Start......... -> {}", number);
            TokenRequestContext request = new TokenRequestContext();
            request.addScopes("https://ossrdbms-aad.database.windows.net/.default");
            AccessToken accessToken;
            if (tokenTimeoutInSeconds > 0) {
                accessToken = tokenCredential.getToken(request).block(Duration.ofSeconds(tokenTimeoutInSeconds));
            } else {
                accessToken = tokenCredential.getTokenSync(request);
            }
            LOGGER.info("[TokenCredential less thread] End......... -> {}, exp at {}", number, accessToken.getExpiresAt().toLocalDateTime().toString());
        });
        acquireTokenThread.setPriority(Thread.MIN_PRIORITY);
        acquireTokenThread.setName("Use token conn less thread - " + number);
        acquireTokenThread.start();
    }
}
