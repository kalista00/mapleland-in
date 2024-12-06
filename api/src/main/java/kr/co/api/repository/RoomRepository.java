package kr.co.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.api.model.Room;

public interface RoomRepository extends JpaRepository<Room, UUID> {
	List<Room> findAllByOrderByCreatedAtDesc();
}
