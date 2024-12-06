package kr.co.core.service;

import kr.co.core.model.User;

public interface LogLoginService {
	void logSuccess(User user, String ipAddress);
}
