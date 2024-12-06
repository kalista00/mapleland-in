package kr.co.api.service;

import java.util.List;
import java.util.UUID;

import kr.co.api.model.Room;

public interface RoomCacheService {
	List<Room> findAllRooms();

	Room createRoom(Room room);

	void deleteRoom(UUID id);
}
