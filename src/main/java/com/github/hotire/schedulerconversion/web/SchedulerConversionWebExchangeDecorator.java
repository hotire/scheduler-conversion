package com.github.hotire.schedulerconversion.web;

import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class SchedulerConversionWebExchangeDecorator extends ServerWebExchangeDecorator {

  public SchedulerConversionWebExchangeDecorator(
    ServerWebExchange delegate) {
    super(delegate);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getRequiredAttribute(String name) {
    if (RouterFunctions.REQUEST_ATTRIBUTE.equals(name)) {
      ServerRequest serverRequest = getDelegate().getRequiredAttribute(name);
      return (T) new SchedulerConversionServerRequestWrapperDecorator(serverRequest);
    }
    return super.getRequiredAttribute(name);
  }
}
