package kr.co.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.co.core.repository.UserLoginLogRepository;
import kr.co.core.security.CorsConfig;
import kr.co.core.security.MyAuthenticationEntryPoint;
import kr.co.core.security.MyOAuth2UserService;
import kr.co.core.security.component.CookieHandler;
import kr.co.core.security.component.JwtTokenProvider;
import kr.co.core.security.filter.TokenAuthenticationFilter;
import kr.co.core.security.handler.OAuth2AuthenticationFailureHandler;
import kr.co.core.security.handler.OAuth2AuthenticationSuccessHandler;
import kr.co.core.service.LogLoginServiceImpl;
import kr.co.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CookieHandler cookieHandler;
	private final UserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserLoginLogRepository userLoginLogRepository;
	private final UserService userService;
	private final CorsConfig corsConfig;
	private final Environment env;
	public static final String LOGIN_URL = "/login";
	public static final String BLOCK_URL = "/block";
	public static final String WAKEUP_URL = "/wakeup";

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
				.authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/login", "/oauth2/**", "/error/**").permitAll().anyRequest().authenticated())
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(new MyAuthenticationEntryPoint(LOGIN_URL, WAKEUP_URL)))
				.oauth2Login(oauth2Login -> oauth2Login.loginProcessingUrl("/admin/oauth2/**")
						.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(new MyOAuth2UserService()))
						.successHandler(new OAuth2AuthenticationSuccessHandler(env.getProperty("default.url"),
								env.getProperty("nickname-url"), getLogLoginService(), userService, jwtTokenProvider,
								cookieHandler))
						.failureHandler(
								new OAuth2AuthenticationFailureHandler(LOGIN_URL, BLOCK_URL, getLogLoginService())))
				.addFilterBefore(new TokenAuthenticationFilter(userDetailsService, jwtTokenProvider, cookieHandler),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private LogLoginServiceImpl getLogLoginService() {
		return new LogLoginServiceImpl(userLoginLogRepository);
	}
}
