package io.github.leonardofrs.user_service.infrastructure.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class LocaleConfig {

  @Bean
  public LocaleContextResolver localeResolver() {
    var resolver = new SessionLocaleResolver();
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }

}
