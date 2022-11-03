package com.example.redisscript.redisscriptdemo.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    private License license() {
        return new License()
                .name("MIT")
                .url("https://opensource.org/licenses/MIT");
    }

    private Info info() {
        return new Info()
                .title("Open API")
                .description("李辉")
                .version("v0.0.1")
                .license(license());
    }

    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
                .description("李辉的")
                .url("https://github.com/damingerdai/hoteler");
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocumentation());
    }


}
