package de.ait.gp.config;


import de.ait.gp.documentation.OpenApiDocumentation;
import de.ait.gp.dto.StandardResponseDto;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OpenAPI openAPI() {
        ResolvedSchema resolvedSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(
                        new AnnotatedType(StandardResponseDto.class).resolveAsRef(false));

        return new OpenAPI()
                .components(new Components()
                        .addSchemas("EmailAndPassword", OpenApiDocumentation.emailAndPassword())
                        .addSecuritySchemes("cookieAuth", OpenApiDocumentation.securityScheme())
                        .addSchemas("StandardResponseDto", resolvedSchema.schema.description("StandardResponseDto")))
                .addSecurityItem(OpenApiDocumentation.buildSecurity())
                .paths(OpenApiDocumentation.buildAuthenticationPath())
                .info(new Info().title("Graduate Project API").version("0.1"));
    }

    @Bean
    public WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
