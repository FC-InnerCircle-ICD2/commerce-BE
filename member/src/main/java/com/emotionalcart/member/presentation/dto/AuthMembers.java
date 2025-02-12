package com.emotionalcart.member.presentation.dto;

import lombok.Data;

public class AuthMembers {

    @Data
    public static class Response {
        private String role;
        private String socialId;
        private String userName;


        public Response(String role, String socialId, String userName) {
            this.role = role;
            this.socialId = socialId;
            this.userName = userName;
        }
    }
}
