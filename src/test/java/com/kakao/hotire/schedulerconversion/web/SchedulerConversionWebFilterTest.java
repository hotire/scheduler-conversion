package com.kakao.hotire.schedulerconversion.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.kakao.hotire.schedulerconversion.config.Mode;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.FilteringWebHandler;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SchedulerConversionWebFilterTest {

  @Test
  public void convert_scheduler_include_mode_contain_pattern() {
    final SchedulerConversionWebFilter schedulerConversionWebFilter =
      this.getSchedulerConversionWebFilter("/api", Mode.INCLUDE);
    final String threadName = Thread.currentThread().getName();
    final ServerWebExchange serverWebExchange = this.getMockServerWebExchange("/api");

    final FilteringWebHandler filteringWebHandler = new FilteringWebHandler(exchange -> {
      assertThat(threadName).isNotEqualTo(Thread.currentThread().getName());
      return Mono.empty();
      }, new ArrayList<>(Collections.singletonList(schedulerConversionWebFilter))
    );

    StepVerifier.create(filteringWebHandler.handle(serverWebExchange)).verifyComplete();
  }

  @Test
  public void maintain_scheduler_include_mode_not_contain_pattern() {
    final SchedulerConversionWebFilter schedulerConversionWebFilter =
      this.getSchedulerConversionWebFilter("/api", Mode.INCLUDE);
    final String threadName = Thread.currentThread().getName();
    final ServerWebExchange serverWebExchange = this.getMockServerWebExchange("/test");

    final FilteringWebHandler filteringWebHandler = new FilteringWebHandler(exchange -> {
      assertThat(threadName).isEqualTo(Thread.currentThread().getName());
      return Mono.empty();
      }, new ArrayList<>(Collections.singletonList(schedulerConversionWebFilter))
    );

    StepVerifier.create(filteringWebHandler.handle(serverWebExchange)).verifyComplete();
  }

  @Test
  public void convert_scheduler_exclude_mode_not_contain_pattern() {
    final SchedulerConversionWebFilter schedulerConversionWebFilter =
      this.getSchedulerConversionWebFilter("/api", Mode.EXCLUDE);
    final String threadName = Thread.currentThread().getName();
    final ServerWebExchange serverWebExchange = this.getMockServerWebExchange("/test");

    final FilteringWebHandler filteringWebHandler = new FilteringWebHandler(exchange -> {
      assertThat(threadName).isNotEqualTo(Thread.currentThread().getName());
      return Mono.empty();
      }, new ArrayList<>(Collections.singletonList(schedulerConversionWebFilter))
    );

    StepVerifier.create(filteringWebHandler.handle(serverWebExchange)).verifyComplete();
  }

  @Test
  public void maintain_scheduler_exclude_mode_contain_pattern() {
    final SchedulerConversionWebFilter schedulerConversionWebFilter =
      this.getSchedulerConversionWebFilter("/api", Mode.EXCLUDE);
    final String threadName = Thread.currentThread().getName();
    final ServerWebExchange serverWebExchange = this.getMockServerWebExchange("/api");

    final FilteringWebHandler filteringWebHandler = new FilteringWebHandler(exchange -> {
      assertThat(threadName).isEqualTo(Thread.currentThread().getName());
      return Mono.empty();
    }, new ArrayList<>(Collections.singletonList(schedulerConversionWebFilter))
    );

    StepVerifier.create(filteringWebHandler.handle(serverWebExchange)).verifyComplete();
  }

  private SchedulerConversionWebFilter getSchedulerConversionWebFilter(final String url, Mode mode) {
    final SchedulerConversionWebFilter schedulerConversionWebFilter = new SchedulerConversionWebFilter();
    schedulerConversionWebFilter.setMode(mode);
    schedulerConversionWebFilter.setPathPatterns(new ArrayList<>(
      Collections.singletonList(new PathPatternParser().parse(url))));
    return schedulerConversionWebFilter;
  }

  private ServerWebExchange getMockServerWebExchange(final String url) {
    final MockServerHttpRequest mockServerHttpRequest = MockServerHttpRequest.method(HttpMethod.GET, url).build();
    return MockServerWebExchange.builder(mockServerHttpRequest).build();
  }
}