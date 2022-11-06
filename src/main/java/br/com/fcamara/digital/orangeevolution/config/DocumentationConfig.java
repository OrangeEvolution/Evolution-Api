package br.com.fcamara.digital.orangeevolution.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class DocumentationConfig {
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("br.com.fcamara.digital.orangeevolution").pathsToMatch("/**").build();
	}

	@Bean
	public OpenAPI orangeEvolutionpenAPI() {
		return new OpenAPI()
				.info(new Info().title("API Orange Evolution")
						.description("Hackathon do Programa de Formação do Grupo FCamara").version("v0.0.1")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation().description("SpringShop Wiki Documentation")
						.url("https://springshop.wiki.github.org/docs"))
				.components(new Components().addSecuritySchemes("bearer_token",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
								.in(SecurityScheme.In.HEADER).bearerFormat("JWT")));
	}
}
