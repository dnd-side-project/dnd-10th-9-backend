package com.dnd.dotchi.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                .description("따봉도치 API")
                .version("v1.0.0");

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }

}
