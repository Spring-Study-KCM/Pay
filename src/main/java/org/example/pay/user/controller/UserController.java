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

@Tag(name = "User Controller", description = "사용자와 연관된 친구 API 입니다.")
@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "친구 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구를 조회했습니다."),
    })
    @GetMapping()
    public ResponseEntity<Object> getStatistics(@RequestBody UserSessionId userSessionId) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "친구 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구를 추가했습니다."),
    })
    @PostMapping("/add")
    public ResponseEntity<Object> addFriend(@RequestBody AddFriendRequest addFriendRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Operation(summary = "친구 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "친구를 삭제했습니다."),
    })
    @DeleteMapping("/add")
    public ResponseEntity<Object> deleteFriend(@RequestBody DeleteFriendRequest deleteFriendRequest) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
