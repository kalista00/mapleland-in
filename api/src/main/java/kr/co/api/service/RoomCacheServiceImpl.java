package kr.co.api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import kr.co.api.model.Room;
import kr.co.api.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomCacheServiceImpl implements RoomCacheService {
	private final RoomRepository repository;

	@Cacheable(value = "roomList")
	@Override
	public List<Room> findAllRooms() {
		return repository.findAllByOrderByCreatedAtDesc();
	}

	@CacheEvict(value = "roomList", allEntries = true)
	@Override
	public Room createRoom(Room room) {
		return repository.save(room);
	}

	@CacheEvict(value = "roomList", allEntries = true)
	public void deleteRoom(UUID id) {
		repository.deleteById(id);
	}

}
