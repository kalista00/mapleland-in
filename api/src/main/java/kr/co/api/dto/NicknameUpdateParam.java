package kr.co.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NicknameUpdateParam(@JsonProperty("nickname") String nickname) {

}
