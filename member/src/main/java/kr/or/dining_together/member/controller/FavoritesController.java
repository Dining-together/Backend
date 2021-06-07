package kr.or.dining_together.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.jpa.entity.Favorites;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.service.FavoritesService;
import kr.or.dining_together.member.service.ResponseService;
import lombok.RequiredArgsConstructor;

@Api(tags = {"4. 즐겨찾기"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member/favorites")
public class FavoritesController {

	private FavoritesService favoritesService;
	private ResponseService responseService;

	@ApiOperation(value = "즐겨찾기 목록 가져오기", notes = "이메일로 즐겨찾기 목록을 가져온다.")
	@GetMapping
	public List<Favorites> getAllFavorites(@RequestParam @ApiParam(value = "이메일 정보", required = true) String email) {
		List<Favorites> favorites = favoritesService.getFavoritesAll(email);
		return favorites;
	}

	@ApiOperation(value = "즐겨찾기 등록", notes = "이메일과 대상정보로 즐겨찾기 등록하기")
	@PostMapping
	public CommonResult postFavorites(@RequestParam @ApiParam(value = "이메일 정보", required = true) String email,
		@RequestParam @ApiParam(value = "등록할 대상 정보", required = true) Long objectId) {
		favoritesService.saveFavorite(email, objectId);
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "즐겨찾기 삭제", notes = "대상정보로 즐겨찾기 삭제하기")
	@DeleteMapping
	public CommonResult deleteFavorites(@RequestParam @ApiParam(value = "이메일 정보", required = true) String email,
		@RequestParam @ApiParam(value = "등록할 대상 정보", required = true) Long objectId) {
		favoritesService.deleteFavorite(email, objectId);
		return responseService.getSuccessResult();
	}
}
