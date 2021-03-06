package com.server.pollingapp.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Jos Wambugu
 * @since 12-04-2021
 * @apiNote <p>
 *      OpenApi is an open-source format for describing and documenting APIs.
 *      I have therefore exposed all my Apis to avoid using postman and to get a visual representation.
 * </p>
 * @see OpenAPI
 */

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig implements WebMvcConfigurer {
    /**
     * Add handlers to serve static resources such as images, js, and, css
     * files from specific locations under web application root, the classpath,
     * and others.
     *
     * @param registry
     * @see ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(info());
    }

    public Info info(){
        return new Info()
                .title("Polling App OpenApi")
                .version("0.0.1-SNAPSHOT")
                .description("Exposure of all Rest Api's")
                .termsOfService("https://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("https://springdoc.org"))
                .contact(contact());
    }
    public Contact contact(){
        return new Contact().name("Jos Wambugu").email("josphatwambugu77@gmail.com").url("https://springdoc.org");
    }

}
