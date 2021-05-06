package com.server.pollingapp.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  private final long MAX_AGE_SECS = 3600;

  /**
   * Configure "global" cross origin request processing. The configured CORS
   * mappings apply to annotated controllers, functional endpoints, and static
   * resources.
   * <p>Annotated controllers can further declare more fine-grained config via
   * {@link CrossOrigin @CrossOrigin}.
   * In such cases "global" CORS configuration declared here is
   * {@link CorsConfiguration#combine(CorsConfiguration) combined}
   * with local CORS configuration defined on a controller method.
   *
   * @param registry
   * @see CorsRegistry
   * @see CorsConfiguration#combine(CorsConfiguration)
   * @since 4.2
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        // .allowCredentials(true)
        .maxAge(MAX_AGE_SECS);
  }
}
