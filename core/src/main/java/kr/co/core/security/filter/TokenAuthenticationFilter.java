package kr.co.core.security.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.core.security.component.CookieHandler;
import kr.co.core.security.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	private final UserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieHandler cookieHandler;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		try {
			String jwt = cookieHandler.extractJwt(request);
			if (jwt != null) {
				jwtTokenProvider.validateTokenAndGetJws(jwt).ifPresentOrElse(jws -> {
					String username = jws.getPayload().getSubject();

					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					// 기존 컨텍스트에 인증 정보를 설정합니다.
					SecurityContextHolder.getContext().setAuthentication(authentication);

					log.info("## SecurityContextHolder Authentication: {}",
							SecurityContextHolder.getContext().getAuthentication());
				}, () -> {
					SecurityContextHolder.clearContext();
				});

			} else {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				SecurityContextHolder.clearContext();
				return;
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("사용자 인증을 설정할 수 없습니다", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			SecurityContextHolder.clearContext();
			return;
		}
	}
}
