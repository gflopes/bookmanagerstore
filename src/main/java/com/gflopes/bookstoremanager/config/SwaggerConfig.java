package com.gflopes.bookstoremanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    private static final String BASE_PACKAGE = "com.gflopes.bookstoremanager";
    private static final String API_TITLE = "BookStoreManager API";
    private static final String API_DESCRIPTION = "BookStore Manager API Project";
    private static final String API_VERSION = "1.0.0";
    private static final String CONTACT_NAME = "\"Gustavo Lopes\"";
    private static final String CONTACT_GITHUB = "\"https://github.com/gflopes/bookstoremanagerapi\"";
    private static final String CONTACT_EMAIL = "\"gflopes22@gmail.com\"";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .build()
                .apiInfo(buildApiInfo());
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .contact(new Contact(CONTACT_NAME, CONTACT_GITHUB, CONTACT_EMAIL))
                .build();
    }
}
