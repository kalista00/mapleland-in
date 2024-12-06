package kr.co.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.core.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByAuthTypeAndAuthId(String authType, String authId);
}
