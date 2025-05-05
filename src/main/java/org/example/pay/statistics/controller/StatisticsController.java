package org.example.pay.statistics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.pay.auth.dto.request.UserSessionId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Statistics Controller", description = "사용자의 통계 관련 API 입니다.")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Operation(summary = "통계 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "통계를 조회했습니다."),
    })
    @GetMapping()
    public ResponseEntity<Object> getStatistics(
            @RequestBody UserSessionId userSessionId,
            @RequestParam(name = "year") Long year,
            @RequestParam(name = "month") Long month) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
