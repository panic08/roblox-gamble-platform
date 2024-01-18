package ru.marthastudios.robloxcasino.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringFoxConfiguration {
    @Bean
    public GroupedOpenApi appApi() {
        return GroupedOpenApi.builder()
                .group("app-api")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/public/**")
                .build();
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RoFlip API")
                        .description("API for RoFlip")
                        .version("v1.0.0"))
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                        .externalDocs(new ExternalDocumentation()
                        .description("Websocket spec for Chat API and Game API (CoinFlip)")
                        .url("https://docs.google.com/document/d/1d7eWj6UGQf3e3pGcl27FUzj5IOGvl95gzaijiJFtGqw/edit?usp=sharing"));
    }
}
