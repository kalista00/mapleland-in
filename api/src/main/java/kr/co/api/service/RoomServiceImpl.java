package kr.co.api.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import kr.co.api.dto.RoomSaveParam;
import kr.co.api.model.Room;
import kr.co.core.dto.RestResult;
import kr.co.core.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
	private final RoomCacheService roomCacheService;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public RestResult<List<Room>> findAll() {
		List<Room> rooms = roomCacheService.findAllRooms();
		return RestResult.success(rooms);
	}

	@Override
	public RestResult<Room> save(RoomSaveParam saveParam) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Room room = null;
		if (authentication != null && authentication.getPrincipal() instanceof User) {
			User currentUser = (User) authentication.getPrincipal();
			room = new Room(saveParam, currentUser);
			roomCacheService.createRoom(room);
		}
		return RestResult.success(room);
	}

	@Override
	public Integer getUserCount(String id) {
		String count = redisTemplate.opsForValue().get("room:" + id + ":userCount");
		if (count != null) {
			return Integer.parseInt(count);
		} else {
			return 0;
		}
	}
}
