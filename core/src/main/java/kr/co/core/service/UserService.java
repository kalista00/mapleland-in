package kr.co.core.service;

import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.core.model.User;

public interface UserService {
	Optional<User> findByAuthTypeAndAuthId(String authType, String authId);

	User save(User user);

	User findById(UUID id);

	User updateNickname(HttpServletRequest request, HttpServletResponse response, String nickname);
}
