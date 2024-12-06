package kr.co.core.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserDto { // TODO : 수정해야됨
    private String id;
    private String nickname;
    private String email;
    private String name;
    private String profileImage;
    private String birthdate;
    private String mobile;
    private String gender;
    private String age;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String resultCode;
        private String message;
        private NaverUserDto response;
    }
}