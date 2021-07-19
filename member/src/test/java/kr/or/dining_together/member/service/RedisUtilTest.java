package kr.or.dining_together.member.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@WebAppConfiguration
public class RedisUtilTest {

	@Autowired
	private RedisUtil redisUtil;

	@Before
	public void setUp() {
		String key = "1";
		String value = "사과";
		redisUtil.setData(key, value);
	}

	@Test
	public void saveAndGetDataTest() {
		String key = "2";
		String value = "당근";
		redisUtil.setData(key, value);

		System.out.println(redisUtil.getData(key));
		assertEquals(value, redisUtil.getData(key));
	}

	@Test
	public void deleteAndCheckDataTest() {
		String value = redisUtil.getData("1");
		System.out.println(value);
		assertNotNull(value);

		redisUtil.deleteData("1");
		String value2 = redisUtil.getData("1");
		System.out.println(value2);
		assertNull(value2);
	}
}
