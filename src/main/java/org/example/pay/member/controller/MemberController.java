package org.example.pay.member.controller;

import org.example.pay.member.dto.JoinDto;
import org.example.pay.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "멤버 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@Operation(summary = "회원가입 API",
		description = "회원가입")
	public void join(@RequestBody JoinDto joinDto) {
		memberService.join(joinDto);
	}

}
