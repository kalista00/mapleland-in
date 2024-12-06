package kr.co.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestResult<T> {
	private boolean success;
	private String message;
	private T data;

	public static <T> RestResult<T> success(T data) {
		return new RestResult<>(true, "Operation successful", data);
	}

	public static <T> RestResult<T> failure(String message) {
		return new RestResult<>(false, message, null);
	}
}
