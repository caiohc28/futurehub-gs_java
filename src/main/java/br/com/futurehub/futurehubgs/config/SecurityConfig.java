// src/main/java/br/com/futurehub/futurehubgs/config/SecurityConfig.java
package br.com.futurehub.futurehubgs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ✅ O H2 precisa de frames e, se você mantiver CSRF, é bom ignorar nos endpoints públicos
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/h2-console/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/actuator/**"
                        )
                        .disable()
                )
                .headers(headers -> headers
                        // ✅ Necessário para o H2 abrir no navegador
                        .frameOptions(frame -> frame.disable())
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ Pré-flight CORS e endpoints públicos
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Swagger/OpenAPI
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ✅ Actuator (liberando health/info; se quiser, troque para “/actuator/**”)
                        .requestMatchers(HttpMethod.GET, "/actuator/health", "/actuator/info").permitAll()

                        // ✅ H2 Console
                        .requestMatchers("/h2-console/**").permitAll()

                        // ✅ Endpoints GET públicos que você já tinha
                        .requestMatchers(HttpMethod.GET, "/areas/**", "/missoes/**", "/ideias/**").permitAll()

                        // 🔒 Demais endpoints exigem autenticação (Basic Auth)
                        .anyRequest().authenticated()
                )
                // 🔑 Basic Auth simples (coerente com spring.security.user no application.yml)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
