package com.kilanov.userswebservice.configurations;

import com.kilanov.userswebservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private UserService userService;
    private Environment environment;
    private BCryptPasswordEncoder encoder;

    public SecurityConfiguration(UserService userService, Environment environment, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.environment = environment;
        this.encoder = encoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(encoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService, environment);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url_path"));

        return http.csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers(mvcMatcherBuilder.pattern("/users")).permitAll();
                            auth.requestMatchers(antMatcher("/h2-console/**")).permitAll();
                        }
                )
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .build();
    }


}
