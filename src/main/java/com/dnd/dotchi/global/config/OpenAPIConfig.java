package com.dnd.dotchi.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    private final String devUrl;

    public OpenAPIConfig(@Value("${dotchi.openapi.dev-url}") final String devUrl) {
        this.devUrl = devUrl;
    }

    @Bean
    public OpenAPI openAPI() {
        final Server server = new Server();
        server.setUrl(devUrl);
        server.setDescription("따봉도치 API");

        final Info info = new Info()
                .title("Dotchi API")
                .version("v1.0.0")
                .description("따봉도치 API");

        final SecurityScheme securityScheme = new SecurityScheme()
                .type(Type.HTTP)
                .in(In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Bearer JWT");

        final SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(List.of(securityRequirement));
    }

}
