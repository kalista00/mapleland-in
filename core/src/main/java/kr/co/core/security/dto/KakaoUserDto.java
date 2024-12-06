package kr.co.core.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserDto {

	private Attributes attributes;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Attributes {
		private Long id;
		private Properties properties;
		@JsonProperty("kakao_account")
		private KakaoAccount kakaoAccount;

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class Properties {
			private String nickname;

			@JsonProperty("profile_image")
			private String profileImage;

			@JsonProperty("thumbnail_image")
			private String thumbnailImage;
		}

		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@JsonIgnoreProperties(ignoreUnknown = true)
		public static class KakaoAccount {

			@JsonProperty("profile_nickname_needs_agreement")
			private Boolean profileNicknameNeedsAgreement;

			@JsonProperty("profile_image_needs_agreement")
			private Boolean profileImageNeedsAgreement;

			@JsonProperty("profile")
			private Profile profile;

			@JsonProperty("is_email_valid")
			private Boolean isEmailValid;

			@JsonProperty("is_email_verified")
			private Boolean isEmailVerified;

			@JsonProperty("email")
			private String email;

			@Data
			@AllArgsConstructor
			@NoArgsConstructor
			@JsonIgnoreProperties(ignoreUnknown = true) // 인식하지 못하는 필드를 무시
			public static class Profile {
				private String nickname;

				@JsonProperty("thumbnail_image_url")
				private String thumbnailImageUrl;

				@JsonProperty("profile_image_url")
				private String profileImageUrl;

				@JsonProperty("is_default_image")
				private Boolean isDefaultImage;

			}
		}
	}
}
