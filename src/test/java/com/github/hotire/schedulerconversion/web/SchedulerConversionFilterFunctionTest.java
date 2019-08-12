package com.github.hotire.schedulerconversion.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerConversionFilterFunctionTest {

  @Mock
  ServerRequest serverRequest;

  @Test
  public void filter() {
    // Given
    final SchedulerConversionFilterFunction filterFunction = new SchedulerConversionFilterFunction();

    // When
    final Mono<ServerResponse> responseMono = filterFunction.filter(serverRequest, request -> {
      assertThat(request).isInstanceOf(SchedulerConversionServerRequestWrapperDecorator.class);
      return ServerResponse.ok().build();
    });

    // Then
    StepVerifier.create(responseMono)
      .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
      .verifyComplete();
  }
}