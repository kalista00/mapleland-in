package kr.co.api.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.co.api.dto.RoomSaveParam;
import kr.co.core.model.User;
import kr.co.core.model.mapping.BaseUIdCUTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "rooms")
@NoArgsConstructor
public class Room extends BaseUIdCUTime {
	private Continent continent;
	private UUID master;
	private String masterName;
	private String name;
	private int capacity;
	private int minLevel;
	private int maxLevel;
	private boolean isActive;

	public Room(RoomSaveParam saveParam, User user) {
		this.continent = saveParam.continent();
		this.master = user.getId();
		this.masterName = user.getNickname();
		this.name = saveParam.name();
		this.capacity = saveParam.capacity();
		this.minLevel = saveParam.levelRange()[0];
		this.maxLevel = saveParam.levelRange()[1];
		this.isActive = true;
	}
}
