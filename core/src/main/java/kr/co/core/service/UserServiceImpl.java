package kr.co.core.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.core.exception.JwtNotFoundException;
import kr.co.core.model.User;
import kr.co.core.repository.UserRepository;
import kr.co.core.security.component.CookieHandler;
import kr.co.core.security.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	private final CookieHandler cookieHandler;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final Environment env;

	@Override
	public Optional<User> findByAuthTypeAndAuthId(String authType, String authId) {
		return userRepository.findByAuthTypeAndAuthId(authType, authId);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
	}

	@Override
	public User updateNickname(HttpServletRequest request, HttpServletResponse response, String nickname) {
		AtomicReference<User> userRef = new AtomicReference<>();

		String jwt = cookieHandler.extractJwt(request);
		jwtTokenProvider.validateTokenAndGetJws(jwt).ifPresentOrElse(jws -> {
			String id = jws.getPayload().getSubject();
			User user = findById(UUID.fromString(id));
			user.setNickname(nickname);
			user.setUpdatedNameYn(true);
			userRef.set(save(user));
			try {
				cookieHandler.setNicknameOnCookie(response, nickname, env.getProperty("default-domain"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, () -> {
			throw new JwtNotFoundException("Exception while changing nickname");
		});
		return userRef.get();
	}
}
