package kr.or.dining_together.member.service;

import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.dining_together.member.dto.EmailInfoDto;
import kr.or.dining_together.member.jpa.entity.EmailInfo;
import kr.or.dining_together.member.jpa.repo.EmailInfoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender emailSender;
	private final EmailInfoRepository emailInfoRepository;
	private final ModelMapper modelMapper;

	@Transactional
	@Async
	public Long sendAuthMail(String to) {
		String key = makeRandomKey();
		EmailInfoDto emailInfoDto = new EmailInfoDto();
		emailInfoDto.setKey(key);
		emailInfoDto.setEmail(to);
		emailInfoDto.setUsed(false);

		Long savedEmailId = emailInfoRepository.save(modelMapper.map(emailInfoDto, EmailInfo.class)).getId();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("[From 회식모아] 이메일 인증");
		message.setText("인증번호는 " + key + " 입니다");
		emailSender.send(message);

		return savedEmailId;
	}

	public Boolean checkEmailVerificationKey(String email, String key) {
		return (emailInfoRepository.findByKey(key).equals(emailInfoRepository.findByEmail(email)));
	}

	private String makeRandomKey() {
		Random r = new Random();
		return Integer.toString((r.nextInt(4589362) + 49311));
	}
}
