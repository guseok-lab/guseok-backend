package com.guseok.guseokbackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String bearerSchemeName = "BearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Guseok API")
                .description("실종자 탐색 서비스 API 문서")
                .version("v1"))
            .addSecurityItem(new SecurityRequirement().addList(bearerSchemeName))
            .components(new Components()
                .addSecuritySchemes(bearerSchemeName, new SecurityScheme()
                    .name(bearerSchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
