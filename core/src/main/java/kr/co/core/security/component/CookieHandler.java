package kr.co.core.security.component;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieHandler {

	public String extractJwt(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwt".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public void setNicknameOnCookie(HttpServletResponse response, String nickname, String domain) throws IOException {
		String encodedNickname = URLEncoder.encode(nickname, "UTF-8");
		Cookie nicknameCookie = new Cookie("nickname", encodedNickname);
		nicknameCookie.setHttpOnly(false); // HttpOnly를 false로 설정
		nicknameCookie.setSecure(true);
		nicknameCookie.setPath("/");
		nicknameCookie.setMaxAge(60 * 60 * 24); // 1일 동안 유효
		nicknameCookie.setDomain(domain);
		response.addCookie(nicknameCookie);
	}

	public void clearJwt(HttpServletResponse response) {
		Cookie jwtCookie = new Cookie("jwt", null);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setMaxAge(0);
		jwtCookie.setPath("/");
		response.addCookie(jwtCookie);
		Cookie nicknameCookie = new Cookie("nickname", null);
		nicknameCookie.setHttpOnly(true);
		nicknameCookie.setMaxAge(0);
		nicknameCookie.setPath("/");
		response.addCookie(nicknameCookie);
	}
}
