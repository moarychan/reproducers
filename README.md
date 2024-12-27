# Reproducer

## 2024-12-26 - Managed Identity token acquisition thread will be blocked when the common pool ForkJoinPool exhausted

Keywords
- Spring Cloud Azure Starter JDBC MySQL 5.19.0
- JPA
- Thread pool `ForkJoinPool` exhausted
- Managed Identity token acquisition thread will be blocked when the common pool ForkJoinPool exhausted

Issues:
- https://github.com/Azure/azure-sdk-for-java/issues/43631
- https://github.com/Azure/azure-sdk-for-java/issues/8626
- https://github.com/Azure/azure-sdk-for-java/issues/22687

Branch: [reproducers/2024/12-26-managed-identity-token-acquisition-thread-blocked](https://github.com/moarychan/reproducers/tree/reproducers/2024/12-26-managed-identity-token-acquisition-thread-blocked)


## Environment Setup

- Create Azure Managed Identity.
- Create an Azure Virtual Machine, or Azure App Service, Azure Container Apps, which supports Managed Identity auth; assign the managed identity to Azure Virtual Machine or other service you chose.
- Create Azure Database for MySQL flexible server, and enabled Microsoft Entra authentication, see more from [Passwordless authentication](https://learn.microsoft.com/en-us/azure/mysql/flexible-server/connect-java?tabs=passwordless).

## Test

Execute below request to simulate no available worker thread can be used in the common pool ForkJoinPool, and send token acquisition request without timeout setting using managed identity credential. 

It submits `9` threads in the common pool, each thread will loop to sleep one second until to `20` times; at the same time, it submits `9` threads with no timeout setting for token requests.
```shell
curl localhost:8080/runner/token/concurrent/less/threads/20/9
```

According to the console log, you can see the simulated working thread occupies the common pool, and the other token acquisition thread is finally activated and successfully returns the token.

Execute the following request to submit `1` thread to send a token acquisition request with a timeout of `30` seconds using the managed identity credentials:
```shell
curl localhost:8080/runner/token/concurrent/1/30
```

Generate stace trace log:
```shell
pid=$(jps | grep MySQLPasswordlessApplication | awk '{print $1}')
log_file="jstack_$(date +'%Y%m%d_%H%M%S').log"
jstack -l $pid > $log_file
```

View the console log and stack trace log, you will see the execution of the threads.

## Workaround

- Review your code, do not run business logic threads under the common pool.
- Update your token credential bean with below definition, it will use a custom thread pool for token acquisition:

  ```java
  @Primary
  @Bean
  TokenCredential test() {
      ExecutorService executorService = Executors.newFixedThreadPool(10, new ThreadFactory() {
          private int count = 0;
          @Override
          public Thread newThread(Runnable runnable) {
              return new Thread(runnable, "Custom*******" + count++);
          }
      });
      return new DefaultAzureCredentialBuilder()
          .managedIdentityClientId("<your-managed-identity-client-id>")
          .executorService(executorService)
          .build();
  }
  ```
