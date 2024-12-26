package org.example.sca.mysql;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.AzureCliCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;

@Configuration
public class AzureConfiguration {

    @Primary
    @Bean
    TokenCredential test() {
//        ExecutorService executorService = Executors.newFixedThreadPool(10, new ThreadFactory() {
//            private int count = 0;
//            @Override
//            public Thread newThread(Runnable runnable) {
//                return new Thread(runnable, "Custom*******" + count++);
//            }
//        });
        return new ManagedIdentityCredentialBuilder()
            .clientId("your-managed-identity-client-id").build();

//        return new DefaultAzureCredentialBuilder()
//            .managedIdentityClientId("00004626-9606-491a-a13f-b79764de6210")
////            .executorService(ForkJoinPool.commonPool())
//            .executorService(executorService)
//            .build();
    }
}
