package com.kakao.hotire.schedulerconversion.config;

import static java.util.stream.Collectors.toList;

import com.kakao.hotire.schedulerconversion.EnableSchedulerConversion;
import com.kakao.hotire.schedulerconversion.web.SchedulerConversionWebFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class SchedulerConversionRegister implements ImportBeanDefinitionRegistrar,
  BeanFactoryAware {

  private static final String PATTERNS_KEY = "scheduler.conversion.patterns";
  private BeanFactory beanFactory;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
    BeanDefinitionRegistry beanDefinitionRegistry) {
    final GenericBeanDefinition genericBeanDefinition = this.createBean(
      SchedulerConversionWebFilter.class);
    final MutablePropertyValues mpv = new MutablePropertyValues();

    mpv.addPropertyValue("mode", this.getMode(annotationMetadata));
    mpv.addPropertyValue("pathPatterns", this.getPatterns());
    genericBeanDefinition.setPropertyValues(mpv);

    beanDefinitionRegistry.registerBeanDefinition("schedulerConversionWebFilter", genericBeanDefinition);
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  private Mode getMode(AnnotationMetadata annotationMetadata) {
    final Map<String, Object> metaData = annotationMetadata.getAnnotationAttributes(
      EnableSchedulerConversion.class.getName());
    final AnnotationAttributes attributes = AnnotationAttributes.fromMap(metaData);
    Objects.requireNonNull(attributes);
    return attributes.getEnum("mode");
  }

  private List<PathPattern> getPatterns() {
    Environment environment = beanFactory.getBean(Environment.class);
    String patterns = environment.getProperty(PATTERNS_KEY);
    return Arrays.stream(Optional.ofNullable(patterns).orElse(Strings.EMPTY).split(","))
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
}
