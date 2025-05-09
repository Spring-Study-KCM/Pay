package org.example.pay.user.dto.request;

import jakarta.validation.constraints.Email;

public record AddFriendRequest(@Email String email) {
}
