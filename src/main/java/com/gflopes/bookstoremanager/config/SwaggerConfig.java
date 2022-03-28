package com.gflopes.bookstoremanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "BookStoreManager API";
    private static final String API_DESCRIPTION = "BookStore Manager API Project";
    private static final String API_VERSION = "1.0.0";
    private static final String CONTACT_NAME = "Gustavo Lopes";
    private static final String CONTACT_GITHUB = "https://github.com/gflopes/bookstoremanagerapi";
    private static final String CONTACT_EMAIL = "gflopes22@gmail.com";
    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION)
                        .contact(new Contact()
                                .name(CONTACT_NAME)
                                .email(CONTACT_EMAIL)
                                .url(CONTACT_GITHUB))

                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)));

    }
}
