package kr.or.dining_together.member.jpa.repo;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import kr.or.dining_together.member.jpa.entity.Customer;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Before
	public void save() {
		//given
		String email = "jifrozen@naver.com";
		String name = "문지언";
		Store store = Store.builder()
			.email(email)
			.name(name)
			.password(passwordEncoder.encode("test1111"))
			.roles(Collections.singletonList("ROLE_USER"))
			.documentChecked(true)
			.build();

		String email2 = "qja9605@naver.com";
		String name2 = "신태범";
		Customer customer = Customer.builder()
			.email(email2)
			.name(name2)
			.password(passwordEncoder.encode("test1111"))
			.roles(Collections.singletonList("ROLE_USER"))
			.age(23)
			.gender("MALE")
			.build();
		//when
		userRepository.save(store);
		userRepository.save(customer);

	}

	@Test
	public void findByEmail() {
		//given
		String email = "jifrozen@naver.com";
		String name = "문지언";

		//when
		Optional<User> user = userRepository.findByEmail(email);
		//then

		System.out.println(user.get());
		assertNotNull(user.get());
		assertTrue(user.isPresent());
		assertEquals(user.get().getName(), name);
	}

	@Test
	public void findById() {
		//given
		long id = 1;
		String name = "문지언";

		//when
		Optional<User> user = userRepository.findById(id);
		//then

		assertTrue(user.isPresent());
		assertEquals(user.get().getName(), name);
	}

	@Test
	public void changePassword() {
		String email = "jifrozen@naver.com";
		String name = "문지언";
		String newPassword = "1234";
		Optional<User> user = userRepository.findByEmail(email);
		String currentPassword = user.get().getPassword();

		userRepository.updatePassword(newPassword, email);

		assertNotEquals(newPassword, currentPassword);
	}
}