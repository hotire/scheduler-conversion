package com.github.hotire.schedulerconversion.web;



import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.REQUEST_ATTRIBUTE;

import java.lang.reflect.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerConversionServerRequestWrapperDecoratorTest {
  @Mock
  private ServerRequest serverRequest;

  @Test
  public void bodyToMono() {
    // Given
    final SchedulerConversionServerRequestWrapperDecorator decorator = new SchedulerConversionServerRequestWrapperDecorator(serverRequest);

    // When
    when(serverRequest.bodyToMono(String.class))
      .thenReturn(Mono.just("test"));
    final Mono<String> mono = decorator.bodyToMono(String.class);

    // Then
    StepVerifier.create(mono).expectNext("test").verifyComplete();
  }

  @Test
  public void bodyToMono_byReference() {
    // Given
    final ParameterizedTypeReference<String> reference = new ParameterizedTypeReference<String>() {
      @Override
      public Type getType() {
        return super.getType();
      }
    };

    final SchedulerConversionServerRequestWrapperDecorator decorator = new SchedulerConversionServerRequestWrapperDecorator(serverRequest);

    // When
    when(serverRequest.bodyToMono(reference))
      .thenReturn(Mono.just("test"));
    final Mono<String> mono = decorator.bodyToMono(reference);

    // Then
    StepVerifier.create(mono).expectNext("test").verifyComplete();
  }

  @Test
  public void bodyToFlux() {
    // Given
    final SchedulerConversionServerRequestWrapperDecorator decorator = new SchedulerConversionServerRequestWrapperDecorator(serverRequest);

    // When
    when(serverRequest.bodyToFlux(String.class))
      .thenReturn(Flux.just("test"));
    final Flux<String> mono = decorator.bodyToFlux(String.class);

    // Then
    StepVerifier.create(mono).expectNext("test").verifyComplete();
  }

  @Test
  public void bodyToFlux_byReference() {
    // Given
    final ParameterizedTypeReference<String> reference = new ParameterizedTypeReference<String>() {
      @Override
      public Type getType() {
        return super.getType();
      }
    };
    final SchedulerConversionServerRequestWrapperDecorator decorator = new SchedulerConversionServerRequestWrapperDecorator(serverRequest);

    // When
    when(serverRequest.bodyToFlux(reference))
      .thenReturn(Flux.just("test"));
    final Flux<String> mono = decorator.bodyToFlux(reference);

    // Then
    StepVerifier.create(mono).expectNext("test").verifyComplete();
  }


}