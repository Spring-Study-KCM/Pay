package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.FriendRequest;
import org.example.dto.FriendRequestResponse;
import org.example.dto.FriendResponse;
import org.example.entity.User;
import org.example.security.CustomUserPrincipal;
import org.example.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    // 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequest request, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        friendService.sendFriendRequest(user, request);
        return ResponseEntity.ok("친구 요청을 보냈습니다.");
    }

    // 친구 요청 수락
    @PostMapping("/accept/{requestId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long requestId, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        friendService.acceptFriendRequest(user, requestId);
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }

    // 친구 요청 거절
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long requestId, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        friendService.rejectFriendRequest(user, requestId);
        return ResponseEntity.ok("친구 요청을 거절했습니다.");
    }

    // 내 친구 목록 조회
    @GetMapping
    public ResponseEntity<List<FriendResponse>> getMyFriends(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        List<FriendResponse> friends = friendService.getMyFriends(user);
        return ResponseEntity.ok(friends);
    }

    // 받은 친구 요청 목록 조회
    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestResponse>> getPendingRequests(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        List<FriendRequestResponse> requests = friendService.getPendingRequests(user);
        return ResponseEntity.ok(requests);
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Long friendId, Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        friendService.deleteFriend(user, friendId);
        return ResponseEntity.ok("친구를 삭제했습니다.");
    }
}
