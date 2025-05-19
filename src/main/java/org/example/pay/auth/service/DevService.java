package org.example.pay.auth.service;

import org.example.pay.auth.dto.request.JoinRequestDto;
import org.example.pay.auth.dto.request.LoginRequestDto;
import org.example.pay.member.domain.Member;
import org.example.pay.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DevService {

	private final MemberRepository memberRepository;
	private final AuthService authService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final SecurityContextService securityContextService;

	@Transactional
	public String tempLogin(LoginRequestDto tempLoginDto, HttpServletRequest request) {

		Member member = memberRepository.findByEmail(tempLoginDto.email())
			.orElseGet(() -> {
				JoinRequestDto joinRequestDto = new JoinRequestDto(tempLoginDto.email(), "임시유저", tempLoginDto.password());
				return authService.join(joinRequestDto);
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
