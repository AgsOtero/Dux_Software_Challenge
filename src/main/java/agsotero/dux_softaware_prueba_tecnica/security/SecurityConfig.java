package agsotero.dux_softaware_prueba_tecnica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER_PATHS = {"/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/**", "/webjars/swagger-ui/**"};

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil, JwtRequestFilter jwtRequestFilter) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF ya que no es necesario para API stateless
                .csrf(AbstractHttpConfigurer::disable)
                // Configurar las reglas de autorización de URL
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Permitir acceso sin autenticación a /auth/login y /h2-console/**
                                .requestMatchers(SWAGGER_PATHS).permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                // Requerir autenticación para cualquier otra solicitud
                                .anyRequest().authenticated()
                )
                // Deshabilitar form login y basic auth
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // Configurar la gestión de sesiones para ser stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Permitir frames de la misma origen, necesario para la consola H2
                .headers(headers -> headers.frameOptions().sameOrigin());

        // Añadir JWT request filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("test")
                        .password(passwordEncoder().encode("12345"))
                        .roles("USER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
