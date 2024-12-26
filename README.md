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


## Setup

- Create an Azure Virtual Machine, or Azure App Service, Azure Container Apps, which supports Managed Identity auth.
- Create Azure Managed Identity.
- Create Azure Database for MySQL flexible server, and enabled Microsoft Entra authentication.

## Test

Execute below request to simulate no available worker thread can be used in the common pool ForkJoinPool.
```shell
curl localhost:8080/runner/token/concurrent/less/threads/20/9
```

Execute below request to simulate a token acquisition request with timeout set:
```shell
curl localhost:8080/runner/token/concurrent/1/30
```

Generate stace trace log:
```shell
pid=$(jps | grep MySQLPasswordlessApplication | awk '{print $1}')
log_file="jstack_$(date +'%Y%m%d_%H%M%S').log"
jstack -l $pid > $log_file
```

View the threads log and stack trace, you will see the execution of the threads.

Hotfix:
- Review your code, not ran business login thread under the common pool.
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
