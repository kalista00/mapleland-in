package kr.co.core.model.mapping;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseLIdCTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createAt;

	@PrePersist
	protected void onCreate() {
		this.createAt = LocalDateTime.now();
	}
}
