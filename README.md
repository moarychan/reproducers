# Reproducer 

## 2024-12-23 - Inconsistent behavior of Environment binding properties in SCS parent-child contexts

Keywords
- Spring Cloud Stream
- Binding properties from environment variables
- Inconsistent `ConditionalOnProperty` behavior between parent and child application context 

Issues:
- https://github.com/spring-cloud/spring-cloud-stream/issues/3039
- https://github.com/Azure/azure-sdk-for-java/issues/42880

Branch: [reproducers/2024/12-23-inconsistent-behavior-between-parent-and-child-context](https://github.com/moarychan/reproducers/tree/reproducers/2024/12-23-inconsistent-behavior-between-parent-and-child-context)

Prepare to run `eventhubs-kafka-binder` with below 2 breakpoints:

- Add breakpoint under the method `com.example.sca.eventhubs.MyConfig.testBean`.
- Add breakpoint on the line `binderProducingContext.refresh();`, which is in the method `DefaultBinderFactory.initializeBinderContextSimple`.

```java
ConfigurableApplicationContext initializeBinderContextSimple(String configurationName, Map<String, Object> binderProperties,
			BinderType binderType, BinderConfiguration binderConfiguration, boolean refresh) {
        
        ...
        

		if (refresh) {
            ...
            
			binderProducingContext.refresh();
		}

		return binderProducingContext;
	}
```

### Run with below environment variables, the `testBean` will be initialized in parent and child app context


```properties
A_B_CONNECTION_STRING=test
```

The bean `testBean` will be initialized twice, before and after method `binderProducingContext.refresh()` is executed.

### Run with below environment variables, the `testBean` will be initialized in parent app context and the child app context will not have


```properties
A_B_CONNECTIONSTRING=test
```

The bean `testBean` will be initialized once, only before method `binderProducingContext.refresh()` is executed. 

We expect the bean `testBean` also is initialized twice regardless of whether the environment is `A_B_CONNECTION_STRING` or `A_B_CONNECTIONSTRING`.

