package io.github.leonardofrs.user_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

  @Bean
  public OpenAPI getOpenAPI() {
    return new OpenAPI()
        .info(apiInfo());
  }

  private Info apiInfo() {
    return new Info()
        .title("User Management API")
        .version("1.0.0")
        .description("""
            RESTful API for user management.
            """)
        .contact(new Contact()
            .name("User Service Development Team")
            .email("lfavio2@gmail.com")
        );
  }
}
