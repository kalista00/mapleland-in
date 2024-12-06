package kr.co.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import kr.co.api.model.Continent;

public record RoomSaveParam(@JsonProperty("name") String name, @JsonProperty("creator") String creator,
		@JsonProperty("continent") Continent continent, @JsonProperty("capacity") int capacity,
		@JsonProperty("levelRange") int[] levelRange) {
}