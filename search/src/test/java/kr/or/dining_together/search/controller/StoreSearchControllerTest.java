package kr.or.dining_together.search.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import kr.or.dining_together.search.document.Store;
import kr.or.dining_together.search.repository.StoreRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class StoreSearchControllerTest {

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext ctx;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
			.alwaysDo(print())
			.build();

		// Store store = new Store().builder()
		// 	.id("2")
		// 	.title("bbq치킨")
		// 	.description("마시따")
		// 	.storeType("치킨")
		// 	.Latitude("37.5665")
		// 	.Longitude("126.734086")
		// 	.bookmark(false)
		// 	.reviewScore("0")
		// 	.build();
		Store store = new Store().builder()
			.id("2")
			.title("bbq치킨")
			.addr("서강대학교")
			.storeType("치킨")
			.build();

		storeRepository.save(store);
	}

	@Test
	public void postIndex() throws Exception {
		// Store store = new Store().builder()
		// 	.id("1")
		// 	.title("bhc치킨")
		// 	.description("더 마시따")
		// 	.storeType("치킨")
		// 	.Latitude("37.5665")
		// 	.Longitude("126.734086")
		// 	.bookmark(false)
		// 	.reviewScore("0")
		// 	.build();

		Store store = new Store().builder()
			.id("1")
			.title("bhc치킨")
			.addr("서강대학교")
			.storeType("치킨")
			.build();

		Gson gson = new Gson();
		String content = gson.toJson(store);

		mockMvc.perform(post("/search/store")
			.content(content)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	public void deleteIndex() throws Exception {
		assertFalse(storeRepository.findAllByTitleContaining("bbq치킨").isEmpty());

		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
		info.add("id", "2");

		mockMvc.perform(delete("/search/store")
			.params(info)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());

		assertTrue(storeRepository.findById("2").isEmpty());
	}

	@Test
	public void searchIndex() throws Exception {
		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
		info.add("keyword", "치킨");

		mockMvc.perform(get("/search/store")
			.params(info)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}
}
