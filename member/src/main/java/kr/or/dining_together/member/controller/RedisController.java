package kr.or.dining_together.member.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

	// @Autowired
	// private RedisTemplate<String, String> redisTemplate;
	//
	// @PostMapping("/redisTest")
	// public ResponseEntity<?> addRedisKey() {
	// 	ValueOperations<String, String> vop = redisTemplate.opsForValue();
	// 	vop.set("yellow", "banana");
	// 	vop.set("red", "apple");
	// 	vop.set("green", "watermelon");
	// 	return new ResponseEntity<>(HttpStatus.CREATED);
	// }
	//
	// @GetMapping("/redisTest/{key}")
	// public ResponseEntity<?> getRedisKey(@PathVariable String key) {
	// 	ValueOperations<String, String> vop = redisTemplate.opsForValue();
	// 	String value = vop.get(key);
	// 	return new ResponseEntity<>(value, HttpStatus.OK);
	// }
}
