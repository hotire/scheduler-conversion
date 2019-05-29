package com.github.hotire.schedulerconversion.web;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SuppressWarnings("unchecked")
public class SchedulerConversionServerRequestWrapperDecorator extends ServerRequestWrapper {

  public SchedulerConversionServerRequestWrapperDecorator(
    ServerRequest delegate) {
    super(delegate);
  }

  @Override
  public <T> Mono<T> bodyToMono(Class<? extends T> elementClass) {
    return (Mono<T>) super.bodyToMono(elementClass).publishOn(Schedulers.elastic());
  }

  @Override
  public <T> Mono<T> bodyToMono(ParameterizedTypeReference<T> typeReference) {
    return super.bodyToMono(typeReference).publishOn(Schedulers.elastic());
  }

  @Override
  public <T> Flux<T> bodyToFlux(Class<? extends T> elementClass) {
    return (Flux<T>) super.bodyToFlux(elementClass).publishOn(Schedulers.elastic());
  }

  @Override
  public <T> Flux<T> bodyToFlux(ParameterizedTypeReference<T> typeReference) {
    return super.bodyToFlux(typeReference).publishOn(Schedulers.elastic());
  }
}
