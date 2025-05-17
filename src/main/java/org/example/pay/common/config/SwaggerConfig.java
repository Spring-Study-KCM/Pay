package org.example.pay.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@Configuration
public class SwaggerConfig {

	private static final String SECURITY_SCHEME_NAME = "JSESSIONID";

	@Bean
	public OpenAPI springOpenAPI() {
		return new OpenAPI()
			.info(
				new Info().title("Pay API 문서")
					.description("""
						### Pay 서비스 REST API 명세 문서입니다.
						"""))
			.components(authComponent());
	}

	private static Components authComponent() {
		return new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
			new SecurityScheme()
				.name(SECURITY_SCHEME_NAME)
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.COOKIE));
	}
}

