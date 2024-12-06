package kr.co.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.api.dto.RoomSaveParam;
import kr.co.api.dto.RoomUpdateParam;
import kr.co.api.model.Room;
import kr.co.api.service.RoomService;
import kr.co.core.dto.RestResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
	private final RoomService roomService;

	@GetMapping("/api/room")
	public RestResult<List<Room>> findAll() {
		return roomService.findAll();
	}

	@PostMapping("/api/room")
	public RestResult<Room> saveRoom(@RequestBody RoomSaveParam saveParam) {
		log.info("## test json - {}", saveParam);
		return roomService.save(saveParam);
	}

	@GetMapping("/api/room/{query}")
	public RestResult<List<Room>> findRoom(@PathVariable String query) {
		return new RestResult<List<Room>>();
	}

	@DeleteMapping("/api/room/{id}")
	public RestResult<String> deleteRoom(@PathVariable String id) {
		return RestResult.success("success");
	}

	@PatchMapping("/api/room")
	public RestResult<Room> updateRoom(@RequestBody RoomUpdateParam updateParam) {
		return new RestResult<Room>();
	}

	@GetMapping("/api/usercount/{id}")
	public Integer getUserCount(@PathVariable String id) {
		return roomService.getUserCount(id);
	}
}
