# Scheduler Conversion

![conversion](/doc/img/conversion_128.png)

> Convert reactor thread into elastic thread

## How to use 

### Config

```
@EnableSchedulerConversion
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

```

### application.yml

```
scheduler.conversion:
  mode: include
  patterns: '/api/**, /batch/**'
```

### Mode

- include (default)

convert reactor thread into elastic thread only if the url patterns are included.

- exclude

In all cases except this url patterns, reactor thread into elastic thread


```

```

## SchedulerConversion

데코레이터 패턴과 WebFilter 를 이용한 reactor 스레드를 elastic 스레드 전환 

WebFilter / FilterFunction 둘 중 하나를 쓰면 동작한다. :)


### SchedulerConversionWebFilter

reactor 스레드를 elastic 스레드로 전환 시키는 WebFilter (범용적으로 사용 가능)

또한 ServerWebExchange 객체를 SchedulerConversionWebExchangeDecorator로 전환한다.

### SchedulerConversionFilterFunction

reactor 스레드를 elastic 스레드로 전환 시키는 FilterFunction (제한적으로 사용 가능)

또한 ServerWebExchange 객체를 SchedulerConversionServerRequestWrapperDecorator로 전환한다.

~~~
  @Bean
  public RouterFunction<ServerResponse> hello(HelloHandler handler) {
    return route(GET("/hello"), handler::hello)
      .filter(new SchedulerConversionFilterFunction());
  }
~~~

.filter 방식으로 선언한다.

### SchedulerConversionWebExchangeDecorator

기존 ServerWebExchange 객체를 감싸서 데코레이터 한다.

```
public interface ServerWebExchange {
	default <T> T getRequiredAttribute(String name) {
		T value = getAttribute(name);
		Assert.notNull(value, "Required attribute '" + name + "' is missing.");
		return value;
	}
}
```
getRequiredAttribute 메서드를 오버라이딩 한다.


### SchedulerConversionServerRequestWrapperDecorator

기존 ServerRequest 객체를 감싸서 데코레이터 한다.

```
public interface ServerRequest {
  <T> Mono<T> bodyToMono(Class<? extends T> elementClass);
  <T> Mono<T> bodyToMono(ParameterizedTypeReference<T> typeReference);
  <T> Flux<T> bodyToFlux(Class<? extends T> elementClass);
  <T> Flux<T> bodyToFlux(ParameterizedTypeReference<T> typeReference);
}

```

해당 메서드들을 오버라이딩 한다.

이유는 bodyToMono / bodyToFlux 할 경우 reactor 스레드로 전환된다.

