package org.example.pay.member.controller;

import org.example.pay.member.service.MemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "멤버 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MemberController {

	private final MemberService memberService;



}
