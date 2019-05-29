package com.github.hotire.schedulerconversion.web;

import java.util.function.Consumer;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Schedulers;

/**
 *  선언적으로 reactor thread, elastic 으로 스레드 전환
 */
public class SchedulerConversionFilterFunction implements
  HandlerFilterFunction<ServerResponse, ServerResponse> {

  @Override
  public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
    return Mono.create(
      (Consumer<MonoSink<ServerResponse>>) monoSink ->
        next.handle(new SchedulerConversionServerRequestWrapperDecorator(request))
          .subscribe(monoSink::success))
      .subscribeOn(Schedulers.elastic());
  }
}
