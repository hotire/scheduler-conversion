package com.github.hotire.schedulerconversion.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.hotire.schedulerconversion.web.SchedulerConversionWebFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerConversionRegisterTest {

  @Mock
  private Environment environment;

  @Mock
  private AnnotationMetadata annotationMetadata;

  @Test
  public void createBean() {
    final SchedulerConversionRegister register = new SchedulerConversionRegister();
    final GenericBeanDefinition genericBeanDefinition = register.createBean(String.class);
    assertThat(genericBeanDefinition.getBeanClass()).isEqualTo(String.class);
  }

  @Test
  public void registerBeanDefinitions() {
    // Given
    final BeanDefinitionRegistry beanDefinitionRegistry = new GenericApplicationContext();
    final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    final SchedulerConversionRegister register = new SchedulerConversionRegister() {
      @Override
      protected GenericBeanDefinition createBean(@Nullable Class<?> beanClass) {
        return beanDefinition;
      }
    };
    register.setEnvironment(environment);

    // When
    when(environment.getProperty("scheduler.conversion.mode")).thenReturn("include");
    when(environment.getProperty("scheduler.conversion.patterns")).thenReturn("");

    register.registerBeanDefinitions(annotationMetadata, beanDefinitionRegistry);
    final BeanDefinition result = beanDefinitionRegistry.getBeanDefinition(SchedulerConversionWebFilter.class.getSimpleName());

    // Then
    assertThat(result).isEqualTo(beanDefinition);
  }
}