package kr.co.core.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;
	private String loginUrl;
	private String wakeUrl;

	public MyAuthenticationEntryPoint(String loginUrl, String wakeUrl) {
		this.loginUrl = loginUrl;
		this.wakeUrl = wakeUrl;
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.info("## MyAuthenticationEntryPoint request - {}", request.getRequestURI());
		log.info("## MyAuthenticationEntryPoint response - {}", response.getContentType());
		log.info("## MyAuthenticationEntryPoint authException - {}", authException);
	}
}
