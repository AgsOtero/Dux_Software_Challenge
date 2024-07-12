package agsotero.dux_softaware_prueba_tecnica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DUX Software Prueba Técnica")
                        .version("1.0")
                        .description("API para gestionar equipos de fútbol")
                        .termsOfService("Terms of service URL")
                        .license(new License().name("License").url("License URL"))
                        .contact(new Contact()
                                .name("Agustin Otero")
                                .url("https://www.linkedin.com/in/agustin-otero-727391189/")
                                .email("oteroagustin95@gmail.com")));
    }
}
