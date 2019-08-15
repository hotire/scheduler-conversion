package com.github.hotire.schedulerconversion.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RouterFunctions.REQUEST_ATTRIBUTE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerConversionWebExchangeDecoratorTest {

  @Mock
  private ServerWebExchange serverWebExchange;

  @Mock
  private ServerRequest serverRequest;

  @Test
  public void getRequiredAttribute() {
    // Given
    final SchedulerConversionWebExchangeDecorator decorator = new SchedulerConversionWebExchangeDecorator(serverWebExchange);

    // When
    when(serverWebExchange.getRequiredAttribute(REQUEST_ATTRIBUTE))
      .thenReturn(serverRequest);
    final ServerRequest request = decorator.getRequiredAttribute(REQUEST_ATTRIBUTE);

    // Then
    assertThat(request).isInstanceOf(SchedulerConversionServerRequestWrapperDecorator.class);
  }
}