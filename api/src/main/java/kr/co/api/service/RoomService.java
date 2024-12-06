package kr.co.api.service;

import java.util.List;

import kr.co.api.dto.RoomSaveParam;
import kr.co.api.model.Room;
import kr.co.core.dto.RestResult;

public interface RoomService {
	RestResult<List<Room>> findAll();

	RestResult<Room> save(RoomSaveParam saveParam);

	Integer getUserCount(String roomId);
}
