package de.witcom.itsm.serviceaccess.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Value("${app.version:}")
    private String apiVersion;

    @Value("${app.description:}")
    private String apiDescription;
    
    @Bean
    public OpenAPI customOpenAPI() {
    	
    	/*
    	return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearer-key",new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
        )
        .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
        .info(new Info().title("ITSM Service-Access API").version("0.0.1-SNAPSHOT"));
        */
    	return new OpenAPI().info(new Info().title(apiDescription).version(apiVersion));
    	
    }



}