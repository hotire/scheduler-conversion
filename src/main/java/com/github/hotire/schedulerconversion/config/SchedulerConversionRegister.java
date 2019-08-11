package com.github.hotire.schedulerconversion.config;

import static java.util.stream.Collectors.toList;

import com.github.hotire.schedulerconversion.web.SchedulerConversionWebFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class SchedulerConversionRegister implements ImportBeanDefinitionRegistrar,
  EnvironmentAware {

  private static final String PATTERNS_KEY = "scheduler.conversion.patterns";

  private static final String MODE_KEY = "scheduler.conversion.mode";

  private Environment environment;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
    BeanDefinitionRegistry beanDefinitionRegistry) {
    final GenericBeanDefinition genericBeanDefinition = this.createBean(SchedulerConversionWebFilter.class);
    final MutablePropertyValues mpv = new MutablePropertyValues();

    mpv.addPropertyValue("mode", this.getMode());
    mpv.addPropertyValue("pathPatterns", this.getPatterns());
    genericBeanDefinition.setPropertyValues(mpv);

    beanDefinitionRegistry.registerBeanDefinition("schedulerConversionWebFilter", genericBeanDefinition);
  }

  private Mode getMode() {
    final String mode = Optional.ofNullable(environment.getProperty(MODE_KEY)).orElse(Mode.INCLUDE.name());
    return Mode.lookup(mode);
  }

  private List<PathPattern> getPatterns() {
    final String patterns = Optional.ofNullable(environment.getProperty(PATTERNS_KEY)).orElse(Strings.EMPTY);
    return Arrays.stream(patterns.split(","))
      .map(String::trim)
      .filter(Strings::isNotEmpty)
      .map(pattern -> new PathPatternParser().parse(pattern))
      .collect(toList());
  }

  private GenericBeanDefinition createBean(@Nullable Class<?> beanClass) {
    final GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
    genericBeanDefinition.setBeanClass(beanClass);
    return genericBeanDefinition;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}

