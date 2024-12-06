package kr.co.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.core.model.UserLoginLog;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}