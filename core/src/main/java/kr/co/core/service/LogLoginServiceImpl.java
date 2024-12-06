package kr.co.core.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kr.co.core.model.User;
import kr.co.core.model.UserLoginLog;
import kr.co.core.repository.UserLoginLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogLoginServiceImpl implements LogLoginService {
	private final UserLoginLogRepository userLoginLogRepository;

	@Async
	@Override
	public void logSuccess(User user, String ipAddress) {
		UserLoginLog userLoginLog = new UserLoginLog(user, ipAddress);
		userLoginLogRepository.save(userLoginLog);
	}
}
