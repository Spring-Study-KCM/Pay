package org.example.pay.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.pay.auth.dto.request.UserSessionId;
import org.example.pay.user.dto.request.AddFriendRequest;
import org.example.pay.user.dto.request.DeleteFriendRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "사용자의 친구 관련 API 입니다.")
@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "친구 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구를 추가했습니다."),
    })
    @PostMapping("/friend")
    public ResponseEntity<Object> addFriend(@RequestBody final AddFriendRequest addFriendRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "친구 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구를 조회했습니다."),
    })
    @GetMapping("/friend")
    public ResponseEntity<Object> getFriends(@RequestBody final UserSessionId userSessionId) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "친구 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구를 삭제했습니다."),
    })
    @DeleteMapping("/friend")
    public ResponseEntity<Object> deLETEFriend(@RequestBody final DeleteFriendRequest deleteFriendRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
