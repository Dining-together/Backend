package kr.or.dining_together.member;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class MemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSender javaMailSender = new JavaMailSender() {
			@Override
			public MimeMessage createMimeMessage() {
				return null;
			}

			@Override
			public MimeMessage createMimeMessage(InputStream inputStream) throws MailException {
				return null;
			}

			@Override
			public void send(MimeMessage mimeMessage) throws MailException {

			}

			@Override
			public void send(MimeMessage... mimeMessages) throws MailException {

			}

			@Override
			public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

			}

			@Override
			public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

			}

			@Override
			public void send(SimpleMailMessage simpleMailMessage) throws MailException {

			}

			@Override
			public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

			}
		};
		return javaMailSender;
	}
}
