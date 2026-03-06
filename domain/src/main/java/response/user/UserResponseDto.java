package response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.myproject.common.functional.MyFunctionalInterface;
import spring.myproject.entity.user.User;


public class UserResponseDto {

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignInResponse {

        private String code;
        private String message;
        private String accessToken;

        public static SignInResponse of(String code, String message, String accessToken) {
            return new SignInResponse(code, message, accessToken);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignUpResponse {

        private final String code;
        private final String message;

        public static SignUpResponse of(String code, String message){
            return new SignUpResponse(code, message);
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UpdateResponse {

        private final String code;
        private final String message;
        private Long userId;

        public static UpdateResponse of(String code, String message,Long userId) {
            return new UpdateResponse(code, message,userId);
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserResponse {

        private final String code;
        private final String message;
        private Long id;
        private String username;
        private String email;
        private String address;
        private Integer age;
        private String hobby;
        private String nickname;
        private String imageUrl;

        public static UserResponse of(String code, String message) {
            return UserResponse.builder()
                    .code(code)
                    .message(message)
                    .build();
        }

        public static UserResponse from(String code, String message,User user, MyFunctionalInterface functionalInterface) {
            return UserResponse.builder()
                    .code(code)
                    .message(message)
                    .id(user.getId())
                    .age(user.getAge())
                    .address(user.getAddress())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .hobby(user.getHobby())
                    .imageUrl(functionalInterface.execute(user.getProfileImage().getUrl()))
                    .username(user.getUsername())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NicknameCheckResponse {

        private final String code;
        private final String message;

        public static NicknameCheckResponse of(String code, String message){
            return new NicknameCheckResponse(code, message);
        }

    }

    @Builder
    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LogOutResponse {

        private String code;
        private String message;

        public static LogOutResponse of(String code, String message){
            return new LogOutResponse(code, message);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IdCheckResponse {

        private final String code;
        private final String message;

        public static IdCheckResponse of(String code,String message){
            return new IdCheckResponse(code,message);
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GenerateTokenResponse {
        private String code;
        private String message;
        private String accessToken;

        public static GenerateTokenResponse of(String code, String message, String accessToken){
            return new GenerateTokenResponse(code,message,accessToken);
        }


    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmailCertificationResponse {

        private String code;
        private String message;

        public static EmailCertificationResponse of(String code, String message){
            return new EmailCertificationResponse(code,message);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CheckCertificationResponse {

        private String code;
        private String message;

        public static CheckCertificationResponse of(String code, String message){
            return new CheckCertificationResponse(code,message);
        }
    }
}
