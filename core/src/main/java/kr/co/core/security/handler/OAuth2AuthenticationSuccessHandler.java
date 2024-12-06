package kr.co.core.security.handler;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.core.model.User;
import kr.co.core.model.mapping.Role;
import kr.co.core.security.OAuthProvider;
import kr.co.core.security.component.CookieHandler;
import kr.co.core.security.component.JwtTokenProvider;
import kr.co.core.security.dto.GoogleUserDto;
import kr.co.core.security.dto.KakaoUserDto;
import kr.co.core.security.dto.KakaoUserDto.Attributes;
import kr.co.core.security.dto.NaverUserDto;
import kr.co.core.service.LogLoginService;
import kr.co.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final String defaultUrl;
	private final String nicknameUrl;
	private final LogLoginService logLoginService;
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final CookieHandler cookieHandler;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String ipAddress = request.getRemoteAddr();

		log.info("로그인 Principal - {}", authentication.getPrincipal());
		log.info("로그인 권한 - {}", authentication.getAuthorities());
		log.info("로그인 제공자 - {}", authentication.getName());

		User user = saveProfileAndGetToken(authentication.getPrincipal(),
				OAuthProvider.valueOf(authentication.getName().toUpperCase()), ipAddress);
		String token = jwtTokenProvider.getToken(user);
		setCookie(token, user.getNickname(), response);
		response.sendRedirect(user.isUpdatedNameYn() ? defaultUrl : nicknameUrl);
	}

	private User saveProfileAndGetToken(Object principal, OAuthProvider provider, String ipAddress) {
		log.info("제공자: {}", provider.name());
		ObjectMapper objectMapper = new ObjectMapper();
		User user;

		switch (provider) {
		case KAKAO:
			KakaoUserDto kakao = objectMapper.convertValue(principal, KakaoUserDto.class);
			Attributes attributes = kakao.getAttributes();
			log.info("속성: {}", attributes);
			user = saveAndGetToken(provider.name(), attributes.getId().toString(),
					attributes.getKakaoAccount().getEmail(), attributes.getProperties().getNickname(), ipAddress);
			break;

		case NAVER:
			NaverUserDto naver = objectMapper.convertValue(principal, NaverUserDto.class);
//			token = saveAndGetToken(provider.name(), naver.getId(), naver.getEmail(),  ipAddress);
			user = null;
			break;

		case GOOGLE:
			GoogleUserDto google = objectMapper.convertValue(principal, GoogleUserDto.class);
//			token = saveAndGetToken(provider.name(), google.getId(), google.getEmail(), ipAddress);
			user = null;
			break;

		default:
			throw new IllegalArgumentException("지원하지 않는 OAuth 제공자: " + provider);
		}

		return user;
	}

	private User saveAndGetToken(String authType, String authId, String email, String nickname, String ipAddress) {
		Optional<User> userOptional = userService.findByAuthTypeAndAuthId(authType, authId);
		User user = userOptional.orElseGet(() -> saveUser(authType, authId, nickname, email));
		logLoginService.logSuccess(user, ipAddress);
		return user;
	}

	private User saveUser(String authType, String authId, String nickname, String email) {
		User user = User.builder().authId(authId).authType(authType).role(Role.USER).nickname(nickname).email(email)
				.build();
		log.info("TEST################## - {}", user);
		userService.save(user);
		return user;
	}

	private void setCookie(String token, String nickname, HttpServletResponse response) throws IOException {
		// JWT 쿠키 생성 및 설정
		Cookie jwtCookie = new Cookie("jwt", token);
		jwtCookie.setHttpOnly(true); // 클라이언트 측 스크립트에서 쿠키 접근 제한
		jwtCookie.setSecure(true); // HTTPS에서만 쿠키 전송
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(60 * 60 * 24); // 1일 동안 유효
		response.addCookie(jwtCookie);

		cookieHandler.setNicknameOnCookie(response, nickname, "mapleland.in");

//		String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
//		Cookie nicknameCookie = new Cookie("nickname", encodedNickname);
//		nicknameCookie.setHttpOnly(false); // HttpOnly를 false로 설정
//		nicknameCookie.setSecure(true);
//		nicknameCookie.setPath("/");
//		nicknameCookie.setMaxAge(60 * 60 * 24); // 1일 동안 유효
//		response.addCookie(nicknameCookie);
	}
}
