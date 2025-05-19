package org.example.pay.common.config;

import org.example.pay.auth.converter.JsonAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JsonAuthenticationConverter authenticationConverter;
	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;
	private final LogoutSuccessHandler logoutHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/login", "/auth/**").permitAll()
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.anyRequest().authenticated()
			);
		http
			.formLogin(AbstractHttpConfigurer::disable)
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable);

		//세션 고정 보호
		http
			.sessionManagement(auth -> auth
				.sessionFixation().changeSessionId());

		http.logout(logout -> logout
			.logoutUrl("/auth/logout")
			.logoutSuccessHandler(logoutHandler)
			.deleteCookies("JSESSIONID")
		);

		http.addFilterAt(jsonAuthenticationFilter(http.getSharedObject(AuthenticationConfiguration.class)),
			UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}

	@Bean
	public AuthenticationFilter jsonAuthenticationFilter(AuthenticationConfiguration authConfig) throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter(authConfig.getAuthenticationManager(), authenticationConverter);

		filter.setRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
		filter.setSuccessHandler(successHandler);
		filter.setFailureHandler(failureHandler);

		return filter;
	}


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
