package kr.co.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.api.dto.NicknameUpdateParam;
import kr.co.core.dto.RestResult;
import kr.co.core.model.User;
import kr.co.core.security.component.CookieHandler;
import kr.co.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final UserService service;
	private final CookieHandler cookieHandler;

	@PatchMapping("/api/user/nickname")
	public RestResult<User> update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody NicknameUpdateParam param) {
		log.info("[API][PATCH][/api/user] - {}", param);
		return RestResult.success(service.updateNickname(request, response, param.nickname()));
	}

	@DeleteMapping("api/user/logout")
	public RestResult<String> logout(HttpServletResponse response) {
		cookieHandler.clearJwt(response);
		return RestResult.success("로그아웃 성공");
	}
}
