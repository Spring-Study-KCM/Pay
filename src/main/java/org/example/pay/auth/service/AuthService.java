package org.example.pay.auth.service;

import org.example.pay.member.domain.Member;
import org.example.pay.member.dto.JoinDto;
import org.example.pay.auth.dto.TempLoginDto;
import org.example.pay.member.service.MemberService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final MemberService memberService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final SecurityContextService securityContextService;

	@Transactional
	public String tempLogin(TempLoginDto tempLoginDto, HttpServletRequest request) {

		Member member = memberService.getByEmail(tempLoginDto.email())
			.orElseGet(() -> {
				JoinDto joinDto = new JoinDto(tempLoginDto.email(), "임시유저", tempLoginDto.password());
				return memberService.join(joinDto);
			});

		if (!bCryptPasswordEncoder.matches(tempLoginDto.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		HttpSession session = createSession(request, member);

		securityContextService.saveSecurityContext(session, member);

		return session.getId();
	}

	private HttpSession createSession(HttpServletRequest request, Member member) {
		HttpSession session = request.getSession(true);
		session.setAttribute("loginMemberId", member.getId());
		return session;
	}
}
