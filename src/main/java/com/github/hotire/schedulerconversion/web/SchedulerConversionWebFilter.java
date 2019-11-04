package com.github.hotire.schedulerconversion.web;

import com.github.hotire.schedulerconversion.config.Mode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.RequestPath;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Slf4j
public class SchedulerConversionWebFilter implements WebFilter {

  private List<PathPattern> pathPatterns;

  private Mode mode;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    final RequestPath path = exchange.getRequest().getPath();
    if (matchPathPatterns(path)) {
      ServerWebExchange serverWebExchangeDecorator = new SchedulerConversionWebExchangeDecorator(exchange);
      return chain.filter(serverWebExchangeDecorator).subscribeOn(Schedulers.elastic());
    }
    return chain.filter(exchange);
  }

  private boolean matchPathPatterns(RequestPath path) {
    if (Mode.INCLUDE == mode) {
      return pathPatterns.stream().anyMatch(pathPattern -> pathPattern.matches(path.pathWithinApplication()));
    }
    return pathPatterns.stream().noneMatch(pathPattern -> pathPattern.matches(path.pathWithinApplication()));
  }

  public void setPathPatterns(List<PathPattern> pathPatterns) {
    this.pathPatterns = pathPatterns;
    this.pathPatterns.forEach(pathPattern -> log.info("{} pattern : {} ", mode, pathPattern));
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }
}
