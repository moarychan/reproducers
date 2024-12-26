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

## 2024-12-23 - Inconsistent behavior of Environment binding properties in SCS parent-child contexts

Keywords
- Spring Cloud Stream
- Binding properties from environment variables
- Inconsistent behavior between parent and child application context 

Issues:
- https://github.com/spring-cloud/spring-cloud-stream/issues/3039
- https://github.com/Azure/azure-sdk-for-java/issues/42880

Branch: [reproducers/2024/12-23-inconsistent-behavior-between-parent-and-child-context](https://github.com/moarychan/reproducers/tree/reproducers/2024/12-23-inconsistent-behavior-between-parent-and-child-context)
