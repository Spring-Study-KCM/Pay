package org.example.pay.friend.controller;

import java.util.List;

import org.example.pay.auth.domain.CustomUserDetails;
import org.example.pay.friend.dto.AddFriendRequest;
import org.example.pay.friend.dto.DeleteFriendRequest;
import org.example.pay.friend.dto.FriendResponse;
import org.example.pay.friend.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
@Tag(name = "친구 API")
public class FriendController {
	private final FriendService friendService;

	@PostMapping
	@Operation(summary = "친구 추가 API",
		description = "친구 추가",
		security = {@SecurityRequirement(name = "session")})
	public ResponseEntity<Void> addFriend(@AuthenticationPrincipal CustomUserDetails user,
		AddFriendRequest request) {
		friendService.addFriend(user.getId(), request);
		return ResponseEntity.ok().build();
	}

	@GetMapping
	@Operation(summary = "친구 목록 조회 API",
		description = "친구 목록 조회",
		security = {@SecurityRequirement(name = "session")})
	public ResponseEntity<List<FriendResponse>> getFriends(@AuthenticationPrincipal CustomUserDetails user) {
		List<FriendResponse> response = friendService.getMyFriends(user.getId());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	@Operation(summary = "친구 삭제 API",
		description = "친구 삭제",
		security = {@SecurityRequirement(name = "session")})
	public ResponseEntity<Void> deleteFriend(@AuthenticationPrincipal CustomUserDetails user,
		DeleteFriendRequest request) {
		friendService.deleteFriend(user.getId(), request);
		return ResponseEntity.noContent().build();
	}
}
