package com.hospital.dto;

import com.hospital.entity.User;

/** Safe, password-free projection of a User returned by the API. */
public record UserResponse(Integer id, String name, String email, String mobile) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getMobile());
    }
}
