// package kr.or.dining_together.search.controller;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.time.LocalDateTime;
//
// import org.junit.Before;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.junit4.SpringRunner;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.context.WebApplicationContext;
// import org.springframework.web.filter.CharacterEncodingFilter;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;
//
// import kr.or.dining_together.search.application.config.LocalDateTimeSerializer;
// import kr.or.dining_together.search.document.Auction;
// import kr.or.dining_together.search.repository.AuctionRepository;
//
// @RunWith(SpringRunner.class)
// @AutoConfigureMockMvc
// @SpringBootTest
// public class AuctionSearchControllerTest {
//
// 	@Autowired
// 	private AuctionRepository auctionRepository;
//
// 	@Autowired
// 	private ObjectMapper objectMapper;
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@Autowired
// 	private WebApplicationContext ctx;
//
// 	@Before
// 	public void setup() {
// 		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
// 			.addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ??????
// 			.alwaysDo(print())
// 			.build();
//
// 		Auction auction = new Auction().builder()
// 			.title("??????2")
// 			.userType("??????")
// 			.reservation(LocalDateTime.now())
// 			.build();
//
// 		auctionRepository.save(auction);
// 	}
//
// 	@Test
// 	public void postIndex() throws Exception {
// 		// Auction auction = new Auction().builder()
// 		// 	.id("11")
// 		// 	.title("?????????")
// 		// 	.groupType("??????")
// 		// 	.registerDate(LocalDateTime.now())
// 		// 	.preferredLocation("???????????????")
// 		// 	.preferredMenu("??????")
// 		// 	.preferredPrice(10000)
// 		// 	.build();
//
// 		Auction auction = new Auction().builder()
// 			.title("??????1")
// 			.userType("??????")
// 			.reservation(LocalDateTime.now())
// 			.build();
//
// 		GsonBuilder gsonBuilder = new GsonBuilder();
// 		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
// 		Gson gson = gsonBuilder.setPrettyPrinting().create();
// 		String content = gson.toJson(auction);
//
// 		mockMvc.perform(post("/search/auction")
// 			.content(content)
// 			.contentType(MediaType.APPLICATION_JSON)
// 			.accept(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andDo(print());
// 	}
//
// 	@Test
// 	public void deleteIndex() throws Exception {
// 		assertFalse(auctionRepository.findAllByTitleContaining("??????2").isEmpty());
//
// 		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
// 		info.add("id", "2");
//
// 		mockMvc.perform(delete("/search/auction")
// 			.params(info)
// 			.contentType(MediaType.APPLICATION_JSON)
// 			.accept(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andDo(print());
//
// 		assertTrue(auctionRepository.findById("2").isEmpty());
// 	}
//
// 	@Test
// 	public void searchIndex() throws Exception {
// 		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
// 		info.add("keyword", "??????2");
//
// 		mockMvc.perform(get("/search/auction")
// 			.params(info)
// 			.contentType(MediaType.APPLICATION_JSON)
// 			.accept(MediaType.APPLICATION_JSON))
// 			.andExpect(status().isOk())
// 			.andDo(print());
// 	}
// }
